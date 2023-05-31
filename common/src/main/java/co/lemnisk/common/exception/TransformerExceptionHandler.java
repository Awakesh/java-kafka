package co.lemnisk.common.exception;

import co.lemnisk.common.kafka.KafkaProducerClient;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformerExceptionHandler {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    private static final Logger logger = LoggerFactory.getLogger(TransformerExceptionHandler.class.getName());

    public void handle(TransformerException exception) {
        if (!shouldDropPayload(exception)) {
            sendMessageToDLT(exception);
        }

        logDetails(exception);
    }

    public void handle(TransformerException exception, Span span) {
        handle(exception);

        if (span != null) {
            span.setStatus(StatusCode.ERROR, exception.getMessage());
            span.recordException(exception);
        }
    }

    private void sendMessageToDLT(TransformerException exception) {
        kafkaProducerClient.sendMessageToDLT(exception.getTopic(),
                exception.getKafkaPayload(), exception);
    }

    private void logDetails(TransformerException exception) {
        logger.warn(exception.getMessage());
    }

    private boolean shouldDropPayload(TransformerException exception) {

        return exception.getDropPayload() ||
                exception.getTopic().isEmpty() ||
                exception.getKafkaPayload().isEmpty();
    }
}
