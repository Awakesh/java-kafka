package co.lemnisk.common.customavroserdes;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomSpecificAvroDeserializer<T extends org.apache.avro.specific.SpecificRecord>
        extends SpecificAvroDeserializer<T> implements Deserializer<T> {

    public CustomSpecificAvroDeserializer() {
        super();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(final String topic, final byte[] bytes) {
        T deserializedResult = null;
        try {
            deserializedResult = (T) super.deserialize(topic, bytes);
        }
        catch (Exception ex) {
            System.out.println("Error in deserializing Avro message: " + ex);
        }
        return deserializedResult;
    }

}