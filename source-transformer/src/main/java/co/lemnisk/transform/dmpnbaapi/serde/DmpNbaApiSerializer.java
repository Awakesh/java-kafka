package co.lemnisk.transform.dmpnbaapi.serde;

import co.lemnisk.loggers.DmpNbaEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiInputPayload;
import com.google.gson.Gson;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DmpNbaApiSerializer implements Serializer<DmpNbaApiInputPayload> {

    @Override
    public byte[] serialize(String topic, DmpNbaApiInputPayload data) {
        Gson gson = new Gson();

        if(data == null){
            return null;
        }

        String stringifiedPayload = gson.toJson(data);

        DmpNbaEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, stringifiedPayload);

        return stringifiedPayload.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, DmpNbaApiInputPayload data) {
        return Serializer.super.serialize(topic, headers, data);
    }
}
