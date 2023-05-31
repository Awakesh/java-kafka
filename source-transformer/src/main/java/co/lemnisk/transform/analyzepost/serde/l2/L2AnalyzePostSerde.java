package co.lemnisk.transform.analyzepost.serde.l2;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class L2AnalyzePostSerde implements Serde<String> {

    @Autowired
    L2AnalyzePostSerializer l2AnalyzePostSerializer;

    @Autowired
    L2AnalyzePostDeserializer l2AnalyzePostDeserializer;

    @Override
    public Serializer<String> serializer() {
        return l2AnalyzePostSerializer;
    }

    @Override
    public Deserializer<String> deserializer() {
        return l2AnalyzePostDeserializer;
    }
}
