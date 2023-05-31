package co.lemnisk.transform.dmpsstdata.serde;

import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.loggers.DmpSstEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.transform.dmpsstdata.model.DmpSstDataInputPayload;
import io.opentelemetry.api.trace.Span;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Component
public class DmpSstDataDeserializer implements Deserializer<DmpSstDataInputPayload> {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    private Span span;

    @Override
    public DmpSstDataInputPayload deserialize(String topic, byte[] byteData) {

        if(byteData == null)
            return null;

        String jsonStr;

        String deserializedMessage = new String(byteData, StandardCharsets.UTF_8);

        if(isJsonType(deserializedMessage)) {
            return null;
        }

        DmpSstEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.INCOMING, deserializedMessage);

        DmpSstDataInputPayload dmpSstDataInputPayload = new DmpSstDataInputPayload();

        try {
            jsonStr = deserializedMessage.split(SourceTransformerConstant.PAYLOAD_SEPARATOR)[3];
            Gson gson = new Gson();
            LinkedTreeMap<String, String> data = (LinkedTreeMap<String, String>) gson.fromJson(jsonStr, Object.class);
            dmpSstDataInputPayload.process(deserializedMessage, data.getOrDefault("data", ""));
        }
        catch(Exception e) {
            TransformerException transformerException = new TransformerException(e.getMessage(), topic, deserializedMessage, false);
            transformerExceptionHandler.handle(transformerException, span);
            return null;
        }

        return dmpSstDataInputPayload;
    }

    private boolean isJsonType(String data) {
        return Pattern.compile("^.*\\{\\\\\".*$").matcher(data).matches();
    }

    @Override
    public DmpSstDataInputPayload deserialize(String topic, Headers headers, byte[] data) {
        this.span = TraceHelper.createSpan(tracing, headers, "DmpSstDataDeserializer", topic);

        try {
            return this.deserialize(topic, data);
        }
        catch (Exception e) {
            TraceHelper.handleException(span, e);
            kafkaProducerClient.sendMessageToDLT(topic, "", e);
            return null;
        }
        finally {
            TraceHelper.closeSpan(headers, span);
        }
    }
}
