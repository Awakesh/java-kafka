package co.lemnisk.transform.analyzepost.serde.l1;

import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import co.lemnisk.common.constants.ExceptionMessage;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.exception.TransformerExceptionHandler;
import co.lemnisk.common.kafka.KafkaProducerClient;
import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import io.opentelemetry.api.trace.Span;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class L1AnalyzePostDeserializer implements Deserializer<String> {

    @Autowired
    TransformerExceptionHandler transformerExceptionHandler;

    @Autowired
    Tracing tracing;

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    private Span span = null;

    @Override
    public String deserialize(String topic, byte[] byteData) throws TransformerException {
        if(byteData == null)
            return null;

        String data;

        String deserializedMessage = new String(byteData, StandardCharsets.UTF_8);

        AnalyzePostEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.INCOMING, deserializedMessage);
        if (span != null) {
            span.addEvent(topic + ":" + deserializedMessage);
        }

        String jsonStr = deserializedMessage.split(SourceTransformerConstant.PAYLOAD_SEPARATOR)[3];
        Gson gson = new Gson();
        LinkedTreeMap<String, String> parsedData = (LinkedTreeMap<String, String>) gson.fromJson(jsonStr, Object.class);
        if (parsedData.containsKey("data"))
            data = parsedData.get("data");
        else
            throw new TransformerException(ExceptionMessage.PARSED_DATA_DOES_NOT_CONTAIN_DATA_KEY);

        if (span != null) {
            span.addEvent("Returning " + data);
        }
        return data;
    }

    @Override
    public String deserialize(String topic, Headers headers, byte[] data) {
        span = TraceHelper.createSpan(tracing, headers, "L1AnalyzePostDeserializer", topic);

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

