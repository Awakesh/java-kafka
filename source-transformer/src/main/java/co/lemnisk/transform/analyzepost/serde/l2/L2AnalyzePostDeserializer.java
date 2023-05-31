package co.lemnisk.transform.analyzepost.serde.l2;

import co.lemnisk.loggers.AnalyzePostEventLogger;
import co.lemnisk.common.logging.EventLogger;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
import io.opentelemetry.api.trace.Span;

import java.nio.charset.StandardCharsets;

@Component
public class L2AnalyzePostDeserializer implements Deserializer<String> {

    @Autowired
    Tracing tracing;

    Span span = null;

    @Override
    public String deserialize(String topic, byte[] byteData) {

        if(byteData == null)
            return null;

        String message = new String(byteData, StandardCharsets.UTF_8);
        AnalyzePostEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.INCOMING, message);

        if (span != null) {
            span.addEvent("Message: " + message);
        }

        return message;
    }

    @Override
    public String deserialize(String topic, Headers headers, byte[] data) {
        span = TraceHelper.createSpan(tracing, headers, "L2AnalyzePostDeserializer", topic);
        try {
            return this.deserialize(topic, data);
        }
        catch (Exception e) {
            TraceHelper.handleException(span, e);
            // kafkaProducerClient.sendMessageToDLT(topic, "", e);
            return null;
        }
        finally {
            TraceHelper.closeSpan(headers, span);
        }
    }
}
