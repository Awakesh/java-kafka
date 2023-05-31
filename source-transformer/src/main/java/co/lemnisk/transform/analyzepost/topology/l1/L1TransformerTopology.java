package co.lemnisk.transform.analyzepost.topology.l1;

import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.common.tracing.ValueTransformTraceHelper;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.transform.analyzepost.classifier.L1AnalyzePostClassifier;
import co.lemnisk.common.constants.AnalyzePostDataFormat;
import co.lemnisk.transform.analyzepost.serde.l1.L1AnalyzePostSerde;
import io.opentelemetry.api.trace.Span;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class L1TransformerTopology {

    @Setter
    private StreamsBuilder builder;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    L1AnalyzePostClassifier l1AnalyzePostClassifier;

    @Autowired
    private Tracing tracing;

    @Autowired
    L1AnalyzePostSerde l1AnalyzePostSerde;

    @Value("${kafka.topic.source.analyze_post}")
    private String sourceTopic;

    @Value("${kafka.topic.analyze_post.l1.v1}")
    private String l1V1Topic;

    @Value("${kafka.topic.analyze_post.l1.v2}")
    private String l1V2Topic;

    @Value("${kafka.topic.analyze_post.l1.v3}")
    private String l1V3Topic;

    @Value("${kafka.topic.analyze_post.l1.default}")
    private String l1DefaultTopic;

    private final Predicate<String, String> isV3Type = (key, value) ->
            L1AnalyzePostClassifier.getDataType(value).equals(AnalyzePostDataFormat.V3);

    private final Predicate<String, String> isV2Type = (key, value) ->
            L1AnalyzePostClassifier.getDataType(value).equals(AnalyzePostDataFormat.V2);

    private final Predicate<String, String> isV1Type = (key, value) ->
            L1AnalyzePostClassifier.getDataType(value).equals(AnalyzePostDataFormat.V1);

    public void transform(){
        KStream<String, String> l1Stream = builder.stream(sourceTopic,
                Consumed.with(Serdes.String(), l1AnalyzePostSerde));

        // L1 classifier for moving the data to different topics on the basis of different data structure.
        l1Stream.filter((key, value) -> value != null)
                .split()
                .branch(isV3Type,
                        Branched.withConsumer(ks ->
                        ks.transformValues(() -> new ValueTransformTraceHelper<String, String>(tracing, TracingConstant.SpanNames.L1_TRANSFORMER_V3, sourceTopic) {
                            @Override
                            public String transformImpl(String data, Span span) {
                                span.setAttribute(TracingConstant.Tags.PAYLOAD_TYPE, AnalyzePostDataFormat.V3);
                                span.addEvent(String.format("%s -----> %s", sourceTopic, l1V3Topic));
                                return data;
                            }
                        })
                        .to(l1V3Topic, Produced.with(Serdes.String(), l1AnalyzePostSerde))))
                .branch(isV2Type,
                        Branched.withConsumer(ks ->
                        ks.transformValues(() -> new ValueTransformTraceHelper<String, String>(tracing, TracingConstant.SpanNames.L1_TRANSFORMER_V2, sourceTopic) {
                            @Override
                            public String transformImpl(String data, Span span) {
                                span.setAttribute(TracingConstant.Tags.PAYLOAD_TYPE, AnalyzePostDataFormat.V2);
                                span.addEvent(String.format("%s -----> %s", sourceTopic, l1V2Topic));
                                return data;
                            }
                        })
                        .to(l1V2Topic, Produced.with(Serdes.String(), l1AnalyzePostSerde))))
                .branch(isV1Type,
                        Branched.withConsumer(ks ->
                        ks.transformValues(() -> new ValueTransformTraceHelper<String, String>(tracing, TracingConstant.SpanNames.L1_TRANSFORMER_V1, sourceTopic) {
                            @Override
                            public String transformImpl(String data, Span span) {
                                span.setAttribute(TracingConstant.Tags.PAYLOAD_TYPE, AnalyzePostDataFormat.V1);
                                span.addEvent(String.format("%s -----> %s", sourceTopic, l1V1Topic));
                                return data;
                            }
                        })
                        .to(l1V1Topic, Produced.with(Serdes.String(), l1AnalyzePostSerde))))
                .defaultBranch(Branched.withConsumer(ks->
                    ks.transformValues(() -> new ValueTransformTraceHelper<String, String>(tracing, TracingConstant.SpanNames.L1_TRANSFORMER_UNKNOWN, sourceTopic) {
                        @Override
                        public String transformImpl(String data, Span span) {
                            span.setAttribute(TracingConstant.Tags.PAYLOAD_TYPE, AnalyzePostDataFormat.UNKNOWN);
                            span.addEvent(String.format("%s -----> %s", sourceTopic, l1DefaultTopic));
                            return data;
                        }
                    })
                    .to(l1DefaultTopic)));
    }
}
