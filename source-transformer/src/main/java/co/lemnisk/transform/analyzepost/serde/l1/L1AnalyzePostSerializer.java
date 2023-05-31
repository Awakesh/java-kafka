package co.lemnisk.transform.analyzepost.serde.l1;

import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class L1AnalyzePostSerializer implements Serializer<String> {

    @Override
    public byte[] serialize(String topic, String data) {
            AnalyzePostEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, data);
            return data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, String data) {
        return this.serialize(topic, data);
    }

    @Override
    public void close() {
        //nothing to do
    }
}
