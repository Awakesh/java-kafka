package co.lemnisk.consumer.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GAValidationException extends RuntimeException {

    private String topic;
    private String kafkaPayload;
    private Boolean dropPayload;

    public GAValidationException(String message) {
        super(message);
        this.dropPayload = true;
    }

    public GAValidationException(String message, String topic, String kafkaPayload, boolean dropPayload) {
        super(message);
        this.topic = topic;
        this.kafkaPayload = kafkaPayload;
        this.dropPayload = dropPayload;
    }
}