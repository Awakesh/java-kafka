package co.lemnisk.stream;

import co.lemnisk.DataTransformer;
import co.lemnisk.common.customavroserdes.CustomSpecificAvroSerde;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.tracing.TransformTracingHelper;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.common.kafka.util.MessageParser;
import co.lemnisk.loggers.DestTransformerInputEventLogger;
import com.google.gson.Gson;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@NoArgsConstructor
public class DestTransformerStreamTopology {

    @Value("${kafka.topic.source}")
    private String sourceTopic;

    @Autowired
    DataTransformer dataTransformer;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    Tracing tracing;

    @Autowired
    MessageParser messageParser;

    private final Gson gson = new Gson();

    public Topology builder(Map<String, String> conf, boolean isKey) {

        StreamsBuilder builder = new StreamsBuilder();

        final CustomSpecificAvroSerde<SpecificRecord> avroSerde = new CustomSpecificAvroSerde<>();
        avroSerde.configure(conf, isKey);

        KStream<String, SpecificRecord> stream = builder.stream(sourceTopic,
                Consumed.with(Serdes.String(), avroSerde));

        stream.transform(() -> new TransformTracingHelper<String, SpecificRecord, KeyValue<String, SpecificRecord>>(tracing, "DestTransformerStreamTopology", sourceTopic) {

            @Override
            public KeyValue<String, SpecificRecord> transformImpl(String key, SpecificRecord value, Span span) {
                logIncomingEvent(key, value);
                try {
                    String payload = gson.toJson(dataTransformer.castAndTransformEventData(value, span));
                    messageParser.parseJson(payload, span);
                }
                catch (Exception ex) {
                    TraceHelper.handleException(span, ex);
                    TransformerException transformerException = new TransformerException(ex.getMessage(), sourceTopic, value.toString(), false);
                    transformerExceptionHandler.handle(transformerException, span);
                }
                return null;
            }

        });

        return builder.build();
    }

    private void logIncomingEvent(String key, SpecificRecord value) {
        DestTransformerInputEventLogger.getInstance().log(sourceTopic, EventLogger.MESSAGE_FLOW.INCOMING, value.toString());
    }

}
