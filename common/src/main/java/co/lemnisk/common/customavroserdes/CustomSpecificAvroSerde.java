package co.lemnisk.common.customavroserdes;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.annotation.InterfaceStability;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@InterfaceStability.Unstable
public class CustomSpecificAvroSerde<T extends org.apache.avro.specific.SpecificRecord> extends SpecificAvroSerde<T>
        implements Serde<T> {

    private final Serde<T> inner;

    public CustomSpecificAvroSerde() {
        inner = Serdes.serdeFrom(new CustomSpecificAvroSerializer<T>(), new CustomSpecificAvroDeserializer<T>());
    }

    @Override
    public Serializer<T> serializer() {
        return inner.serializer();
    }

    @Override
    public Deserializer<T> deserializer() {
        return inner.deserializer();
    }

    @Override
    public void configure(final Map<String, ?> serdeConfig, final boolean isSerdeForRecordKeys) {
        inner.serializer().configure(serdeConfig, isSerdeForRecordKeys);
        inner.deserializer().configure(serdeConfig, isSerdeForRecordKeys);
    }

    @Override
    public void close() {
        inner.serializer().close();
        inner.deserializer().close();
    }

}