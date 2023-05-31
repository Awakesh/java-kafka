package co.lemnisk.transform.analyzepost.topology.l2;

import co.lemnisk.EventDeduplication;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.common.avro.model.event.identify.web.IdentifyWeb;
import co.lemnisk.common.avro.model.event.page.Page;
import co.lemnisk.common.avro.model.event.track.web.TrackWeb;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.common.tracing.TransformTracingHelper;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.analyzepost.builder.V1DataBuilder;
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
public class L2V1TransformerTopology {

    @Setter
    private StreamsBuilder builder;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Autowired
    L2AnalyzePostSerde l2AnalyzePostSerde;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    Map<String, Serde<?>> specificAvroSerde;

    @Value("${kafka.topic.analyze_post.l1.v1}")
    private String l1V1Topic;

    @Value("${kafka.topic.sink}")
    private String sinkTopic;

    @Value("${kafka.topic.sink.default}")
    private String defaultSinkTopic;

    @Value("${analyze_post.state_store.name}")
    private String stateStoreName;

    private static final Predicate<String, V1DataBuilder> isIdentifyEvent = (key, dataBuilder) ->
            dataBuilder.getEventType().equals(EventType.IDENTIFY);
    private static final Predicate<String, V1DataBuilder> isPageEvent = (key, dataBuilder) ->
            dataBuilder.getEventType().equals(EventType.PAGE);
    private static final Predicate<String, V1DataBuilder> isTrackEvent = (key, dataBuilder) ->
            dataBuilder.getEventType().equals(EventType.TRACK);

    public void transform(Map<String, Serde<?>> specificAvroSerde) {
        this.specificAvroSerde = specificAvroSerde;
    
        KStream<String, String> l2V1Stream = builder.stream(l1V1Topic,
                Consumed.with(Serdes.String(), l2AnalyzePostSerde));

        l2V1Stream.mapValues(this::getV1DataBuilder)
                .filter((key, value) -> value != null)
                .split()
                .branch(isTrackEvent, Branched.withConsumer(this::branchTrackStream))
                .branch(isPageEvent, Branched.withConsumer(this::branchPageStream))
                .branch(isIdentifyEvent, Branched.withConsumer(this::branchIdentifyStream))
                .defaultBranch(Branched.withConsumer(stream -> stream.mapValues(V1DataBuilder::getInputPayload).to(defaultSinkTopic)));
    }

    private void branchTrackStream(KStream<String, V1DataBuilder> trackKStream) {
        trackKStream
            .transform(() -> new TransformTracingHelper<String, V1DataBuilder, KeyValue<String, TrackWeb>>(tracing, TracingConstant.SpanNames.L2_V1_TRACK_WEB, l1V1Topic) {
                @Override
                public KeyValue<String, TrackWeb> transformImpl(String key, V1DataBuilder value, Span span) {
                    return buildTrackEventData(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, (Produced<String, TrackWeb>) Produced.with(Serdes.String(), this.specificAvroSerde.get("trackWebSerde")));
    }

    private void branchPageStream(KStream<String, V1DataBuilder> pageKStream) {
        pageKStream
            .transform(() -> new TransformTracingHelper<String, V1DataBuilder, KeyValue<String, Page>>(tracing, TracingConstant.SpanNames.L2_V1_PAGE, l1V1Topic) {
                @Override
                public KeyValue<String, Page> transformImpl(String key, V1DataBuilder value, Span span) {
                    return buildPageEventData(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, (Produced<String, Page>) Produced.with(Serdes.String(), this.specificAvroSerde.get("pageSerde")));
    }

    private void branchIdentifyStream(KStream<String, V1DataBuilder> identifyKStream) {
        identifyKStream
            .transform(() -> new TransformTracingHelper<String, V1DataBuilder, KeyValue<String, IdentifyWeb>>(tracing, TracingConstant.SpanNames.L2_V1_IDENTIFY_WEB, l1V1Topic) {
                @Override
                public KeyValue<String, IdentifyWeb> transformImpl(String key, V1DataBuilder value, Span span) {
                    return buildIdentifyEventData(key, value, span);
                }})
            .transform(() -> new EventDeduplication<>(stateStoreName, (k, v) -> (String) v.getMessageId()), stateStoreName)
            .filter((key, value) -> value != null)
            .to(sinkTopic, (Produced<String, IdentifyWeb>) Produced.with(Serdes.String(), this.specificAvroSerde.get("identifyWebSerde")));
    }

    private V1DataBuilder getV1DataBuilder(String inputPayload) {
        V1DataBuilder v1DataBuilder;
        try {
            v1DataBuilder = new V1DataBuilder(inputPayload);
            autowireCapableBeanFactory.autowireBean(v1DataBuilder);
            return v1DataBuilder;
        }
        catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1V1Topic, inputPayload, false);
            transformerExceptionHandler.handle(transformerException);
            return null;
        }
    }

    private KeyValue<String, TrackWeb> buildTrackEventData(String key, V1DataBuilder v1DataBuilder, Span span) {
        TrackWeb trackData = null;
        CharSequence outputKey = null;
        try {
            trackData = v1DataBuilder.buildTrackEventData();
            log(sinkTopic, String.valueOf(trackData));
            TraceHelper.addSpanAttributes(span, (String) trackData.getType(), (String) trackData.getMessageId(), (String) trackData.getContext().getAccountId());
            outputKey =  trackData.getId();
        } catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1V1Topic, v1DataBuilder.getInputPayload(), true);
            transformerExceptionHandler.handle(transformerException, span);
        }
        return KeyValue.pair(String.valueOf(outputKey),  trackData);
    }

    private void log(String topic , String data){
        AnalyzePostEventLogger.getInstance()
                .log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, data);
    }

    private KeyValue<String, Page> buildPageEventData(String key, V1DataBuilder v1DataBuilder, Span span) {
        Page pageData = null;
        CharSequence outputKey = null;
        try {
            pageData = v1DataBuilder.buildPageEventData();
            log(sinkTopic, String.valueOf(pageData));
            TraceHelper.addSpanAttributes(span, (String) pageData.getType(), (String) pageData.getMessageId(), (String) pageData.getContext().getAccountId());
            outputKey =  pageData.getId();
        } catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1V1Topic, v1DataBuilder.getInputPayload(), true);
            transformerExceptionHandler.handle(transformerException, span);
        }
        return KeyValue.pair(String.valueOf(outputKey), pageData);
    }

    private KeyValue<String, IdentifyWeb> buildIdentifyEventData(String key, V1DataBuilder v1DataBuilder, Span span) {
        IdentifyWeb identifyWebData = null;
        CharSequence outputKey = null;
        try {
            identifyWebData = v1DataBuilder.buildIdentifyEventData();
            log(sinkTopic, String.valueOf(identifyWebData));
            TraceHelper.addSpanAttributes(span, (String) identifyWebData.getType(), (String) identifyWebData.getMessageId(), (String) identifyWebData.getContext().getAccountId());
            outputKey = identifyWebData.getId();

        } catch (Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), l1V1Topic, v1DataBuilder.getInputPayload(), true);
            transformerExceptionHandler.handle(transformerException, span);
        }
        return KeyValue.pair(String.valueOf(outputKey), identifyWebData);
    }
}
