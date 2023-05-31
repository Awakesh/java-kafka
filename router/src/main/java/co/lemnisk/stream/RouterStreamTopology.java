package co.lemnisk.stream;

import co.lemnisk.DataMultiplier;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.common.tracing.TransformTracingHelper;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.loggers.RouterEventLogger;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.opentelemetry.api.trace.Span;
import lombok.NoArgsConstructor;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.To;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class RouterStreamTopology {

    @Value("${kafka.topic.source}")
    private String sourceTopic;

    @Value("${kafka.topic.destination}")
    private String destinationTopic;

    @Autowired
    DataMultiplier dataMultiplier;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    public Topology builder(Map<String, String> conf, boolean isKey) {

        StreamsBuilder builder = new StreamsBuilder();

        final CustomSpecificAvroSerde<SpecificRecord> avroSerde = new CustomSpecificAvroSerde<>();
        avroSerde.configure(conf, isKey);

        KStream<String, SpecificRecord> stream = builder.stream(sourceTopic,
                Consumed.with(Serdes.String(), avroSerde));

        KStream<String, SpecificRecord> translated = stream.filter((k, v) -> v != null)
            .transform(() -> new TransformTracingHelper<String, SpecificRecord, KeyValue<String, SpecificRecord>>(tracing, "RouterStreamTopology", sourceTopic) {

                @Override
                public KeyValue<String, SpecificRecord> transformImpl(String key, SpecificRecord value, Span span) {
                    try {
                        logIncomingEvent(key, value);
                        List<KeyValue<String, SpecificRecord>> list = dataMultiplier.castAndCloneEventData(key, value, span);
                        span.addEvent(String.format("castAndCloneEventData yielded %d records", list.size()));
                        list.forEach(record -> {
                            span.addEvent(String.format("%s -> %s", record.key, record.value));
                            if (record.value != null) {
                                logOutgoingEvent(record);
                                context.forward(record.key, record.value);
                            }
                        });
                    } catch (Exception ex) {
                        TraceHelper.handleException(span, ex);
                        TransformerException transformerException = new TransformerException(ex.getMessage(), sourceTopic, value.toString(), false);
                        transformerExceptionHandler.handle(transformerException, span);
                    }
                    return null;
                }
               
            });

        translated.to(destinationTopic, Produced.with(Serdes.String(), avroSerde));

        return builder.build();
    }

    private void logIncomingEvent(String key, SpecificRecord value) {
        RouterEventLogger.getInstance().log(sourceTopic, EventLogger.MESSAGE_FLOW.INCOMING, value.toString());
    }

    private void logOutgoingEvent(KeyValue<String, SpecificRecord> record) {
        RouterEventLogger.getInstance().log(destinationTopic, EventLogger.MESSAGE_FLOW.OUTGOING, record.value.toString());
    }


}
