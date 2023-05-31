package co.lemnisk.transform.analyzepost.topology.l2;

import co.lemnisk.EventDeduplication;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.common.avro.model.event.identify.app.IdentifyApp;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.screen.Screen;
import co.lemnisk.common.avro.model.event.track.app.TrackApp;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.common.tracing.TransformTracingHelper;
import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.analyzepost.builder.V3DataBuilder;
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
public class L2V3TransformerTopology {

    @Setter
    private StreamsBuilder builder;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    L2AnalyzePostSerde l2AnalyzePostSerde;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Value("${kafka.topic.analyze_post.l1.v3}")
    private String l1LatestTopic;

    @Value("${kafka.topic.sink}")
    private String sinkTopic;

    @Value("${kafka.topic.sink.default}")
    private String defaultSinkTopic;

    @Value("${analyze_post.state_store.name}")
    private String stateStoreName;

    @Autowired
    Tracing tracing;

    private Serde<IdentifyWeb> identifyWebSerde;
    private Serde<IdentifyApp> identifyAppSerde;
    private Serde<TrackWeb> trackWebSerde;
    private Serde<TrackApp> trackAppSerde;
    private Serde<Page> pageSerde;
    private Serde<Screen> screenSerde;

    private static final Predicate<String, V3DataBuilder> isIdentifyWebEvent = (key, value) -> value.isIdentifyWebEvent();
    private static final Predicate<String, V3DataBuilder> isIdentifyAppEvent = (key, value) -> value.isIdentifyAppEvent();
    private static final Predicate<String, V3DataBuilder> isTrackAppEvent = (key, value) -> value.isTrackAppEvent();
    private static final Predicate<String, V3DataBuilder> isPageEvent = (key, value) -> value.isPageEvent();
    private static final Predicate<String, V3DataBuilder> isScreenEvent = (key, value) -> value.isScreenEvent();
    private static final Predicate<String, V3DataBuilder> isTrackWebEvent = (key, value) -> value.isTrackWebEvent();

    public void transform(Map<String, String> avroConfig, boolean isKey) {

        initializeAvroSerde(avroConfig, isKey);

        KStream<String, String> l2LatestStream = builder.stream(l1LatestTopic,
                Consumed.with(Serdes.String(), l2AnalyzePostSerde));

        l2LatestStream.mapValues(this::getV3DataBuilder)
            .filter((k, v) -> v != null)
            .split()
                .branch(isIdentifyWebEvent, Branched.withConsumer(this::branchIdentifyWebStream))
                .branch(isIdentifyAppEvent, Branched.withConsumer(this::branchIdentifyAppStream))
                .branch(isTrackWebEvent, Branched.withConsumer(this::branchTrackWebStream))
                .branch(isTrackAppEvent, Branched.withConsumer(this::branchTrackAppStream))
                .branch(isPageEvent, Branched.withConsumer(this::branchPageStream))
                .branch(isScreenEvent, Branched.withConsumer(this::branchScreenStream))
                .defaultBranch(Branched.withConsumer(stream -> stream.mapValues(v3DataBuilderObj -> v3DataBuilderObj.rawData).to(defaultSinkTopic)));
    }

    private void branchIdentifyWebStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, IdentifyWeb>>(tracing, TracingConstant.SpanNames.L2_V3_IDENTIFY_WEB, l1LatestTopic) {
                @Override
                public KeyValue<String, IdentifyWeb> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildIdentifyWebEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), identifyWebSerde));
    }

    private void branchIdentifyAppStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, IdentifyApp>>(tracing, TracingConstant.SpanNames.L2_V3_IDENTIFY_APP, l1LatestTopic) {
                @Override
                public KeyValue<String, IdentifyApp> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildIdentifyAppEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), identifyAppSerde));
    }

    private void branchTrackWebStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, TrackWeb>>(tracing, TracingConstant.SpanNames.L2_V3_TRACK_WEB, l1LatestTopic) {
                @Override
                public KeyValue<String, TrackWeb> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildTrackWebEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), trackWebSerde));
    }

    private void branchTrackAppStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, TrackApp>>(tracing, TracingConstant.SpanNames.L2_V3_TRACK_APP, l1LatestTopic) {
                @Override
                public KeyValue<String, TrackApp> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildTrackAppEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), trackAppSerde));
    }

    private void branchPageStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, Page>>(tracing, TracingConstant.SpanNames.L2_V3_PAGE, l1LatestTopic) {
                @Override
                public KeyValue<String, Page> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildPageEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), pageSerde));
    }

    private void branchScreenStream(KStream<String, V3DataBuilder> stream) {
        stream
            .transform(() -> new TransformTracingHelper<String, V3DataBuilder, KeyValue<String, Screen>>(tracing, TracingConstant.SpanNames.L2_V3_SCREEN, l1LatestTopic) {
                @Override
                public KeyValue<String, Screen> transformImpl(String key, V3DataBuilder value, Span span) {
                    return buildScreenEvent(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((k, v) -> v != null)
            .to(sinkTopic, Produced.with(Serdes.String(), screenSerde));
    }

    private V3DataBuilder getV3DataBuilder(String jsonStr){
        V3DataBuilder v3DataBuilderObj = null;

        try {
            v3DataBuilderObj = new V3DataBuilder(jsonStr);
            autowireCapableBeanFactory.autowireBean(v3DataBuilderObj);
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, jsonStr, false);
            transformerExceptionHandler.handle(transformerException);
        }
        return v3DataBuilderObj;
    }

    private KeyValue<String,IdentifyApp> buildIdentifyAppEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        IdentifyApp identifyAppData = null;
        CharSequence outputKey = null;
        try {
            identifyAppData = v3DataBuilder.buildIdentifyApp();
            log(sinkTopic, String.valueOf(identifyAppData));
            TraceHelper.addSpanAttributes(span, (String) identifyAppData.getType(), (String) identifyAppData.getMessageId(), (String) identifyAppData.getContext().getAccountId());
            outputKey = identifyAppData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), identifyAppData);
    }

    private KeyValue<String,IdentifyWeb> buildIdentifyWebEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        IdentifyWeb identifyWebData = null;
        CharSequence outputKey = null;
        try {
            identifyWebData = v3DataBuilder.buildIdentifyWeb();
            log(sinkTopic, String.valueOf(identifyWebData));
            TraceHelper.addSpanAttributes(span, (String) identifyWebData.getType(), (String) identifyWebData.getMessageId(), (String) identifyWebData.getContext().getAccountId());
            outputKey = identifyWebData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), identifyWebData);
    }

    private KeyValue<String,Page> buildPageEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        Page pageData = null;
        CharSequence outputKey = null;
        try {
            pageData = v3DataBuilder.buildPage();
            log(sinkTopic, String.valueOf(pageData));
            TraceHelper.addSpanAttributes(span, (String) pageData.getType(), (String) pageData.getMessageId(), (String) pageData.getContext().getAccountId());
            outputKey = pageData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), pageData);
    }

    private KeyValue<String,TrackApp> buildTrackAppEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        TrackApp trackAppData = null;
        CharSequence outputKey = null;
        try {
            trackAppData = v3DataBuilder.buildTrackApp();
            log(sinkTopic, String.valueOf(trackAppData));
            TraceHelper.addSpanAttributes(span, (String) trackAppData.getType(), (String) trackAppData.getMessageId(), (String) trackAppData.getContext().getAccountId());
            outputKey = trackAppData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), trackAppData);
    }

    private KeyValue<String,TrackWeb> buildTrackWebEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        TrackWeb trackWebData = null;
        CharSequence outputKey = null;
        try {
            trackWebData = v3DataBuilder.buildTrackWeb();
            log(sinkTopic, String.valueOf(trackWebData));
            TraceHelper.addSpanAttributes(span, (String) trackWebData.getType(), (String) trackWebData.getMessageId(), (String) trackWebData.getContext().getAccountId());
            outputKey = trackWebData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), trackWebData);
    }

    private KeyValue<String,Screen> buildScreenEvent(String key, V3DataBuilder v3DataBuilder, Span span) {
        Screen screenData = null;
        CharSequence outputKey = null;
        try {
            screenData = v3DataBuilder.buildScreen();
            log(sinkTopic, String.valueOf(screenData));
            TraceHelper.addSpanAttributes(span, (String) screenData.getType(), (String) screenData.getMessageId(), (String) screenData.getContext().getAccountId());
            outputKey = screenData.getId();
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1LatestTopic, v3DataBuilder.rawData, true);
            transformerExceptionHandler.handle(transformerException, span);
        }

        return KeyValue.pair(String.valueOf(outputKey), screenData);
    }

    private void log(String topic , String data){
        AnalyzePostEventLogger.getInstance()
                .log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, data);
    }

    private void initializeAvroSerde(Map<String, String> avroConfig, boolean isKey) {

        this.pageSerde = new CustomSpecificAvroSerde<>();
        this.pageSerde.configure(avroConfig, isKey);

        this.trackWebSerde = new CustomSpecificAvroSerde<>();
        this.trackWebSerde.configure(avroConfig, isKey);

        this.trackAppSerde = new CustomSpecificAvroSerde<>();
        this.trackAppSerde.configure(avroConfig, isKey);

        this.identifyWebSerde = new CustomSpecificAvroSerde<>();
        this.identifyWebSerde.configure(avroConfig, isKey);

        this.identifyAppSerde = new CustomSpecificAvroSerde<>();
        this.identifyAppSerde.configure(avroConfig, isKey);

        this.screenSerde = new CustomSpecificAvroSerde<>();
        this.screenSerde.configure(avroConfig, isKey);
    }
}
