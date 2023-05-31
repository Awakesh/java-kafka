package co.lemnisk.transform.dmpnbaapi;

import co.lemnisk.EventDeduplication;
import co.lemnisk.common.avro.model.event.dmpnba.DmpNba;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.tracing.*;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.loggers.DmpNbaEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.statestore.StateStoreConfig;
import co.lemnisk.transform.dmpnbaapi.builder.DmpNbaApiBuilder;
import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiInputPayload;
import co.lemnisk.transform.dmpnbaapi.serde.DmpNbaApiSerde;
import io.opentelemetry.api.trace.Span;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DmpNbaApiTransformerTopology {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    DmpNbaApiSerde dmpNbaApiSerde;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    @Value("${kafka.topic.source.dmp_nba_di_api}")
    private String sourceTopic;

    @Value("${kafka.topic.sink}")
    private String sinkTopic;

    @Value("${kafka.topic.sink.default}")
    private String defaultSinkTopic;

    @Autowired
    EventMonitoring eventMonitoring;

    @Value("${dmp_nba_di_api.state_store.name}")
    private String stateStoreName;

    private Serde<DmpNba> dmpNbaSerde;

    private static final Predicate<String, DmpNbaApiBuilder> isValidEvent = (key, value) -> value.isValidEvent();

    public Topology builder(Map<String, String> avroConfig, boolean isKey) {

        StreamsBuilder builder = new StreamsBuilder();
        initializeAvroSerde(avroConfig, isKey);

        KStream<String, DmpNbaApiInputPayload> stream = builder.stream(sourceTopic,
                Consumed.with(Serdes.String(), dmpNbaApiSerde));

        builder.addStateStore(StateStoreConfig.config(stateStoreName));

        stream
            .filter((key, value) -> value != null)
            .transformValues(() -> new ValueTransformTraceHelper<DmpNbaApiInputPayload, DmpNbaApiBuilder>(tracing, TracingConstant.SpanNames.DMP_NBA_INITIALIZE_BUILDER, sourceTopic) {
                @Override
                public DmpNbaApiBuilder transformImpl(DmpNbaApiInputPayload data, Span span) {
                    return getBuilderData(data, span);
                }})
            .filter((key, value) -> value != null)
            .split()
            .branch(isValidEvent, Branched.withConsumer(this::branchEventStream))
            .defaultBranch(Branched.withConsumer(stream2 -> stream2.mapValues(DmpNbaApiBuilder::getRawData).to(defaultSinkTopic)));

        return builder.build();
    }

    private DmpNbaApiBuilder getBuilderData(DmpNbaApiInputPayload dmpNbaApiInputPayload, Span span) {
        if (dmpNbaApiInputPayload == null) return null;

        DmpNbaApiBuilder dmpNbaApiBuilder ;
        try {
            dmpNbaApiBuilder = new DmpNbaApiBuilder(dmpNbaApiInputPayload, dmpNbaApiInputPayload.getRawData());
            autowireCapableBeanFactory.autowireBean(dmpNbaApiBuilder);
            dmpNbaApiBuilder.isValidData();
        }
        catch (Exception e) {
            dmpNbaApiBuilder = null;
            TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, dmpNbaApiInputPayload.getRawData(), false);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return dmpNbaApiBuilder;
    }

    private void branchEventStream(KStream<String, DmpNbaApiBuilder> stream) {

        stream
            .transform(() -> new TransformTracingHelper<String,DmpNbaApiBuilder,KeyValue<String, DmpNba>>(tracing, TracingConstant.SpanNames.DMP_NBA_EVENT_TRANSFORMER, sourceTopic) {
                @Override
                public KeyValue<String, DmpNba> transformImpl(String key, DmpNbaApiBuilder value, Span span) {
                    return buildEventStream(key, value, span);
                }
                
            })
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), dmpNbaSerde));
    }

    private KeyValue<String, DmpNba> buildEventStream(String key, DmpNbaApiBuilder dmpNbaApiBuilder, Span span) {
        DmpNba dmpNbaData = null;
        CharSequence outputKey = null;
        try {
            addMetrics(dmpNbaApiBuilder);
            dmpNbaData = dmpNbaApiBuilder.buildEventData();
            log(sinkTopic, String.valueOf(dmpNbaData));
            TraceHelper.addSpanAttributes(span, (String) dmpNbaData.getType(), (String) dmpNbaData.getMessageId(), (String) dmpNbaData.getContext().getAccountId());
            outputKey = dmpNbaData.getMessageId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, dmpNbaApiBuilder.getRawData(), false);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey),dmpNbaData);
    }

    private void addMetrics(DmpNbaApiBuilder dmpNbaApiBuilder) {
        eventMonitoring.addCdpMetric(dmpNbaApiBuilder.getContext().getAccountId().toString(),
            dmpNbaApiBuilder.getContext().getSrcId().toString(),
            dmpNbaApiBuilder.getEventName(),
            dmpNbaApiBuilder.eventType(),
            Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(dmpNbaApiBuilder.getContext().getAccountId().toString(),
            dmpNbaApiBuilder.getContext().getSrcId().toString(),
            dmpNbaApiBuilder.getEventName(),
            dmpNbaApiBuilder.getProperties(),
            dmpNbaApiBuilder.eventType());
    }

    private void log(String topic , String data){
        DmpNbaEventLogger.getInstance()
                .log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, data);
    }

    private void initializeAvroSerde(Map<String, String> avroConfig, boolean isKey) {

        this.dmpNbaSerde = new CustomSpecificAvroSerde<>();
        this.dmpNbaSerde.configure(avroConfig, isKey);

    }
}
