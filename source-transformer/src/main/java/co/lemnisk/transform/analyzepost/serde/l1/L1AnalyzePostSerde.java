package co.lemnisk.transform.analyzepost.serde.l1;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class L1AnalyzePostSerde implements Serde<String> {
    @Autowired
    L1AnalyzePostSerializer l1AnalyzePostSerializer;

    @Autowired
    L1AnalyzePostDeserializer l1AnalyzePostDeserializer;

    @Override
    public Serializer<String> serializer() {
        return l1AnalyzePostSerializer;
    }

    @Override
    public Deserializer<String> deserializer() {
        return l1AnalyzePostDeserializer;
    }
}
