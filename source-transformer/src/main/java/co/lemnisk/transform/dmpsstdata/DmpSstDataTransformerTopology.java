package co.lemnisk.transform.dmpsstdata;

import co.lemnisk.EventDeduplication;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.tracing.*;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.loggers.DmpSstEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.statestore.StateStoreConfig;
import co.lemnisk.transform.dmpsstdata.builder.DmpSstDataBuilder;
import co.lemnisk.transform.dmpsstdata.model.DmpSstDataInputPayload;
import co.lemnisk.transform.dmpsstdata.serde.DmpSstDataSerde;
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
public class DmpSstDataTransformerTopology {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    DmpSstDataSerde dmpSstDataSerde;

    @Autowired
    EventMonitoring eventMonitoring;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    @Value("${dmp_sst_data.state_store.name}")
    private String stateStoreName;
    
    @Value("${kafka.topic.source.dmp_sst_data}")
    private String sourceTopic;

    @Value("${kafka.topic.sink}")
    private String sinkTopic;

    @Value("${kafka.topic.sink.default}")
    private String defaultSinkTopic;

    private Serde<Page> pageSerde;
    private Serde<TrackWeb> trackWebSerde;
    private Serde<IdentifyWeb> identifyWebSerde;

    public Topology builder(Map<String, String> avroConfig, boolean isKey) {

        StreamsBuilder builder = new StreamsBuilder();
        initializeAvroSerde(avroConfig, isKey);

        KStream<String, DmpSstDataInputPayload> stream = builder.stream(sourceTopic, Consumed.with(Serdes.String(), dmpSstDataSerde));

        builder.addStateStore(StateStoreConfig.config(stateStoreName));

        KStream<String, DmpSstDataBuilder<?>> translated = stream
            .filter((key, value) -> value != null)
            .transformValues(() -> new ValueTransformTraceHelper<DmpSstDataInputPayload, DmpSstDataBuilder<?>>(tracing, TracingConstant.SpanNames.DMP_SST_INITIALIZE_BUILDER, sourceTopic) {
                @Override
                public DmpSstDataBuilder<?> transformImpl(DmpSstDataInputPayload dmpSstDataInputPayload, Span span) {
                    try {
                        dmpSstDataInputPayload.validateWithException();
                        DmpSstDataBuilder<?> dataBuilder = dmpSstDataInputPayload.getDataBuilder();
                        autowireCapableBeanFactory.autowireBean(dataBuilder);
                        return dataBuilder;
                    } catch (Exception e) {
                        TraceHelper.handleException(span, e);
                        TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, String.valueOf(dmpSstDataInputPayload), true);
                        transformerExceptionHandler.handle(transformerException, span);
                        return null;
                    }
                }
            }).filter((key, value) -> value != null);

        Predicate<String, DmpSstDataBuilder<?>> isPageEvent = (key, value) -> value.eventType().equals(EventType.PAGE);
        Predicate<String, DmpSstDataBuilder<?>> isTrackEvent = (key, value) -> value.eventType().equals(EventType.TRACK);
        Predicate<String, DmpSstDataBuilder<?>> isIdentifyEvent = (key, value) -> value.eventType().equals(EventType.IDENTIFY);

        translated.split()
            .branch(isPageEvent, Branched.withConsumer(this::branchPageStream))
            .branch(isTrackEvent, Branched.withConsumer(this::branchTrackStream))
            .branch(isIdentifyEvent, Branched.withConsumer(this::branchIdentifyStream))
            .defaultBranch(Branched.withConsumer(str -> str
                .transformValues(() -> new ValueTransformTraceHelper<DmpSstDataBuilder<?>, String>(tracing, TracingConstant.SpanNames.DMP_SST_UNKNOWN, sourceTopic) {
                    @Override
                    public String transformImpl(DmpSstDataBuilder<?> data, Span span) {
                        return data.getRawData();
                    }})
                .to(defaultSinkTopic)));

        return builder.build();
    }

    private void addMetrics(DmpSstDataBuilder<?> dataBuilder) {
        eventMonitoring.addCdpMetric(dataBuilder.getAccountId().toString(),
                dataBuilder.getSrcId(),
                dataBuilder.getEventName(),
                dataBuilder.eventType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(dataBuilder.getAccountId().toString(),
                dataBuilder.getSrcId(),
                dataBuilder.getEventName(),
                dataBuilder.getProperties(),
                dataBuilder.eventType());
    }

    private void branchPageStream(KStream<String, DmpSstDataBuilder<?>> stream) {

        stream
            .transform(() -> new TransformTracingHelper<String, DmpSstDataBuilder<?>, KeyValue<String, Page>>(tracing, TracingConstant.SpanNames.DMP_SST_PAGE, sourceTopic) {
                @Override
                public KeyValue<String, Page> transformImpl(String key, DmpSstDataBuilder<?> builder, Span span) {
                    return buildPageEvents(key, builder, span);
                }
            })
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, Produced.with(Serdes.String(), pageSerde));
    }

    private KeyValue<String, Page> buildPageEvents(String key, DmpSstDataBuilder<?> builder, Span span) {
        CharSequence outputKey = null;
        try {
            addMetrics(builder);
            builder.constructData();
            outputKey = builder.getId();
            Page pageData = (Page) builder.getData();
            TraceHelper.addSpanAttributes(span, (String) pageData.getType(), (String) pageData.getMessageId(), (String) pageData.getContext().getAccountId());
            DmpSstEventLogger.getInstance().log(sinkTopic, EventLogger.MESSAGE_FLOW.OUTGOING, String.valueOf(builder.getData()));
        }
        catch (Exception e){
            TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, builder.getRawData(), false);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey),(Page) builder.getData());
    }

    private void branchTrackStream(KStream<String, DmpSstDataBuilder<?>> stream) {

        stream
            .transform(() -> new TransformTracingHelper<String, DmpSstDataBuilder<?>, KeyValue<String, TrackWeb>>(tracing, TracingConstant.SpanNames.DMP_SST_TRACK_WEB, sourceTopic) {
                @Override
                public KeyValue<String, TrackWeb> transformImpl(String key, DmpSstDataBuilder<?> builder, Span span) {
                    return buildTrackEvents(key, builder, span);
                }
            })
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, Produced.with(Serdes.String(), trackWebSerde));
    }

    private KeyValue<String, TrackWeb> buildTrackEvents(String key, DmpSstDataBuilder<?> builder, Span span) {
        CharSequence outputKey = null;
        try {
            addMetrics(builder);
            builder.constructData();
            outputKey = builder.getId();
            TrackWeb trackWebData = (TrackWeb) builder.getData();
            TraceHelper.addSpanAttributes(span, (String) trackWebData.getType(), (String) trackWebData.getMessageId(), (String) trackWebData.getContext().getAccountId());
            DmpSstEventLogger.getInstance().log(sinkTopic, EventLogger.MESSAGE_FLOW.OUTGOING, String.valueOf(builder.getData()));
        }
        catch (Exception e){
            TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, builder.getRawData(), false);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey),(TrackWeb) builder.getData());
    }

    private void branchIdentifyStream(KStream<String, DmpSstDataBuilder<?>> stream) {

        stream
            .transform(() -> new TransformTracingHelper<String, DmpSstDataBuilder<?>, KeyValue<String, IdentifyWeb>>(tracing, TracingConstant.SpanNames.DMP_SST_IDENTIFY_WEB, sourceTopic) {
                @Override
                public KeyValue<String, IdentifyWeb> transformImpl(String key, DmpSstDataBuilder<?> builder, Span span) {
                    return buildIdentifyEvents(key, builder, span);
                }
            })
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, Produced.with(Serdes.String(), identifyWebSerde));
    }

    private KeyValue<String, IdentifyWeb> buildIdentifyEvents(String key, DmpSstDataBuilder<?> builder, Span span) {
        CharSequence outputKey = null;
        try {
            addMetrics(builder);
            builder.constructData();
            outputKey = builder.getId();
            IdentifyWeb identifyWebData = (IdentifyWeb) builder.getData();
            TraceHelper.addSpanAttributes(span, (String) identifyWebData.getType(), (String) identifyWebData.getMessageId(), (String) identifyWebData.getContext().getAccountId());
            DmpSstEventLogger.getInstance().log(sinkTopic, EventLogger.MESSAGE_FLOW.OUTGOING, String.valueOf(builder.getData()));
        }
        catch (Exception e){
            TransformerException transformerException = new TransformerException(e.getMessage(), sourceTopic, builder.getRawData(), false);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey),(IdentifyWeb) builder.getData());
    }

    private void initializeAvroSerde(Map<String, String> avroConfig, boolean isKey) {

        this.pageSerde = new CustomSpecificAvroSerde<>();
        this.pageSerde.configure(avroConfig, isKey);

        this.trackWebSerde = new CustomSpecificAvroSerde<>();
        this.trackWebSerde.configure(avroConfig, isKey);

        this.identifyWebSerde = new CustomSpecificAvroSerde<>();
        this.identifyWebSerde.configure(avroConfig, isKey);
    }
}
