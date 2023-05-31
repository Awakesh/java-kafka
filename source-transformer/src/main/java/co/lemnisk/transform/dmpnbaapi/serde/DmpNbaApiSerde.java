package co.lemnisk.transform.dmpnbaapi.serde;

import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiInputPayload;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DmpNbaApiSerde implements Serde<DmpNbaApiInputPayload> {

    @Autowired
    DmpNbaApiSerializer dmpNbaApiSerializer;

    @Autowired
    DmpNbaApiDeserializer dmpNbaApiDeserializer;


    @Override
    public Serializer<DmpNbaApiInputPayload> serializer() {
        return dmpNbaApiSerializer;
    }

    @Override
    public Deserializer<DmpNbaApiInputPayload> deserializer() {
        return dmpNbaApiDeserializer;
    }
}
