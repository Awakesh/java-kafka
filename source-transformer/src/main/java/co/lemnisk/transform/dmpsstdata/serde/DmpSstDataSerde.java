package co.lemnisk.transform.dmpsstdata.serde;

import co.lemnisk.transform.dmpsstdata.model.DmpSstDataInputPayload;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DmpSstDataSerde implements Serde<DmpSstDataInputPayload> {

    @Autowired
    DmpSstDataSerializer dmpSstDataSerializer;

    @Autowired
    DmpSstDataDeserializer dmpSstDataDeserializer;

    @Override
    public Serializer<DmpSstDataInputPayload> serializer() {
        return dmpSstDataSerializer;
    }

    @Override
    public Deserializer<DmpSstDataInputPayload> deserializer() {
        return dmpSstDataDeserializer;
    }
}
