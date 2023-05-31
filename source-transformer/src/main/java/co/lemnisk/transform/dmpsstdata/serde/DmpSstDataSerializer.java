package co.lemnisk.transform.dmpsstdata.serde;

import co.lemnisk.loggers.DmpSstEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.dmpsstdata.model.DmpSstDataInputPayload;
import com.google.gson.Gson;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DmpSstDataSerializer implements Serializer<DmpSstDataInputPayload> {

    @Override
    public byte[] serialize(String topic, DmpSstDataInputPayload data) {

        Gson gson = new Gson();

        if(data == null){
            return null;
        }

        String stringifiedPayload = gson.toJson(data);

        DmpSstEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, stringifiedPayload);

        return stringifiedPayload.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, DmpSstDataInputPayload data) {
        return this.serialize(topic, data);
    }

}
