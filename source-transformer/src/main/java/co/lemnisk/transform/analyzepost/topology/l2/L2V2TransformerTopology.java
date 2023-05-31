package co.lemnisk.transform.analyzepost.topology.l2;

import co.lemnisk.EventDeduplication;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.common.tracing.TransformTracingHelper;
import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.analyzepost.builder.V2DataBuilder;
import co.lemnisk.transform.analyzepost.serde.l2.L2AnalyzePostSerde;
import io.opentelemetry.api.trace.Span;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@NoArgsConstructor
public class L2V2TransformerTopology {

    @Setter
    private StreamsBuilder builder;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    L2AnalyzePostSerde l2AnalyzePostSerde;

    @Autowired
    EventMonitoring eventMonitoring;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    Tracing tracing;

    @Value("${kafka.topic.analyze_post.l1.v2}")
    private String l1NewTopic;

    @Value("${kafka.topic.sink}")
    private String sinkTopic;

    @Value("${kafka.topic.sink.default}")
    private String defaultSinkTopic;

    @Value("${analyze_post.state_store.name}")
    private String stateStoreName;

    private Serde<IdentifyApp> identifyAppSerde;
    private Serde<TrackApp> trackAppSerde;
    private Serde<Screen> screenSerde;

    private static final Predicate<String, V2DataBuilder> isIdentifyAppEvent = (key, value) -> value.isIdentifyEvent();
    private static final Predicate<String, V2DataBuilder> isTrackAppEvent = (key, value) -> value.isTrackEvent();
    private static final Predicate<String, V2DataBuilder> isScreenEvent = (key, value) -> value.isScreenEvent();

    public void transform(Map<String, String> avroConfig, boolean isKey) {

        initializeAvroSerde(avroConfig, isKey);

        KStream<String, String> l2V2Stream = builder.stream(l1NewTopic,
                Consumed.with(Serdes.String(), l2AnalyzePostSerde));

        l2V2Stream.mapValues(this::getV2DataBuilder)
                .filter((key, value) -> value != null)
                .split()
                .branch(isIdentifyAppEvent, Branched.withConsumer(this::branchIdentifyAppStream))
                .branch(isTrackAppEvent, Branched.withConsumer(this::branchTrackAppStream))
                .branch(isScreenEvent, Branched.withConsumer(this::branchScreenStream))
                .defaultBranch(Branched.withConsumer(stream -> stream.mapValues(v2DataBuilderObj -> v2DataBuilderObj.rawData).to(defaultSinkTopic)));
    }

    private void branchIdentifyAppStream(KStream<String, V2DataBuilder> stream) {

        stream
            .transform(() -> new TransformTracingHelper<String, V2DataBuilder, KeyValue<String, IdentifyApp>>(tracing, TracingConstant.SpanNames.L2_V2_IDENTIFY_APP, l1NewTopic) {
                @Override
                public KeyValue<String, IdentifyApp> transformImpl(String key, V2DataBuilder value, Span span) {
                    return buildIdentifyEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), identifyAppSerde));
    }

    private void branchTrackAppStream(KStream<String, V2DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V2DataBuilder, KeyValue<String, TrackApp>>(tracing, TracingConstant.SpanNames.L2_V2_TRACK_APP, l1NewTopic) {
                @Override
                public KeyValue<String, TrackApp> transformImpl(String key, V2DataBuilder value, Span span) {
                    return buildTrackEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), trackAppSerde));
    }

    private void branchScreenStream(KStream<String, V2DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V2DataBuilder, KeyValue<String, Screen>>(tracing, TracingConstant.SpanNames.L2_V2_SCREEN, l1NewTopic) {
                @Override
                public KeyValue<String, Screen> transformImpl(String key, V2DataBuilder value, Span span) {
                    return buildScreenEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), screenSerde));
    }

    private V2DataBuilder getV2DataBuilder(String jsonStr){
        V2DataBuilder v2DataBuilderObj;

        try {
            v2DataBuilderObj = new V2DataBuilder(jsonStr);
            autowireCapableBeanFactory.autowireBean(v2DataBuilderObj);
        }
        catch (Exception e) {
            v2DataBuilderObj = null;
            TransformerException transformerException = new TransformerException(e.getMessage(), l1NewTopic, jsonStr, false);
            transformerExceptionHandler.handle(transformerException);
        }

        try{
            if (v2DataBuilderObj !=null) {
                v2DataBuilderObj.validateWithException();
                v2DataBuilderObj.addMetrics(v2DataBuilderObj, eventMonitoring);
            }
        }
        catch (Exception e){
            TransformerException transformerException = new TransformerException(e.getMessage(), l1NewTopic, jsonStr, true);
            transformerExceptionHandler.handle(transformerException);
        }

        return v2DataBuilderObj;
    }

    private  KeyValue<String, IdentifyApp> buildIdentifyEvent(String key, V2DataBuilder v2DataBuilder, Span span) {
        IdentifyApp identifyAppData = null;
        CharSequence outputKey = null;
        try {
            identifyAppData = v2DataBuilder.buildIdentifyApp();
            log(sinkTopic, String.valueOf(identifyAppData));
            TraceHelper.addSpanAttributes(span, (String) identifyAppData.getType(), (String) identifyAppData.getMessageId(), (String) identifyAppData.getContext().getAccountId());
            outputKey = identifyAppData.getId();
        }
        catch (Exception e) {

            identifyAppData = null;
            TransformerException transformerException = new TransformerException(e.getMessage(), l1NewTopic, v2DataBuilder.rawData, false);
            transformerExceptionHandler.handle(transformerException);
        }
        return KeyValue.pair(String.valueOf(outputKey), identifyAppData);
    }

    private  KeyValue<String, TrackApp> buildTrackEvent(String key, V2DataBuilder v2DataBuilder, Span span) {
        TrackApp trackAppData = null;
        CharSequence outputKey = null;
        try {
            trackAppData = v2DataBuilder.buildTrackApp();
            log(sinkTopic, String.valueOf(trackAppData));
            TraceHelper.addSpanAttributes(span, (String) trackAppData.getType(), (String) trackAppData.getMessageId(), (String) trackAppData.getContext().getAccountId());
            outputKey = trackAppData.getId();
        }
        catch (Exception e) {

            trackAppData = null;
            TransformerException transformerException = new TransformerException(e.getMessage(), l1NewTopic, v2DataBuilder.rawData, false);
            transformerExceptionHandler.handle(transformerException);
        }
        return KeyValue.pair(String.valueOf(outputKey), trackAppData);
    }

    private  KeyValue<String, Screen> buildScreenEvent(String key, V2DataBuilder v2DataBuilder, Span span) {
        Screen screenData = null;
        CharSequence outputKey = null;
        try {
            screenData = v2DataBuilder.buildScreen();
            log(sinkTopic, String.valueOf(screenData));
            TraceHelper.addSpanAttributes(span, (String) screenData.getType(), (String) screenData.getMessageId(), (String) screenData.getContext().getAccountId());
            outputKey = screenData.getId();
        }
        catch (Exception e) {

            screenData = null;
            TransformerException transformerException = new TransformerException(e.getMessage(), l1NewTopic, v2DataBuilder.rawData, false);
            transformerExceptionHandler.handle(transformerException);
        }
        return KeyValue.pair(String.valueOf(outputKey), screenData);
    }

    private void log(String topic , String data){
        AnalyzePostEventLogger.getInstance()
                .log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, data);
    }

    private void initializeAvroSerde(Map<String, String> avroConfig, boolean isKey) {

        this.trackAppSerde = new CustomSpecificAvroSerde<>();
        this.trackAppSerde.configure(avroConfig, isKey);

        this.identifyAppSerde = new CustomSpecificAvroSerde<>();
        this.identifyAppSerde.configure(avroConfig, isKey);

        this.screenSerde = new CustomSpecificAvroSerde<>();
        this.screenSerde.configure(avroConfig, isKey);
    }

}
