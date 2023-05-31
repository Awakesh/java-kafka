package co.lemnisk.common.customavroserdes;

import co.lemnisk.common.appcontext.ApplicationContextProvider;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.annotation.InterfaceStability;
import org.apache.kafka.common.serialization.Serializer;


@InterfaceStability.Unstable
public class CustomSpecificAvroSerializer<T extends org.apache.avro.specific.SpecificRecord>
        extends SpecificAvroSerializer<T> implements Serializer<T> {

    TransformerExceptionHandler transformerExceptionHandler = ApplicationContextProvider.getApplicationContext().getBean(TransformerExceptionHandler.class);

    public CustomSpecificAvroSerializer() {
       super();
    }

    @Override
    public byte[] serialize(final String topic, final T record) {
        byte[] serializedResult = null;
        try {
            serializedResult = super.serialize(topic, record);
        }
        catch (Exception ex) {
            TransformerException transformerException = new TransformerException(ex.getMessage(), topic, record.toString(), false);
            transformerExceptionHandler.handle(transformerException);
        }
        return serializedResult;
    }

}