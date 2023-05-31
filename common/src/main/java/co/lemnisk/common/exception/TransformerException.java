package co.lemnisk.common.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransformerException extends RuntimeException {

    private String topic;
    private String kafkaPayload;
    private Boolean dropPayload;

    public TransformerException(String message) {
        super(message);
        this.dropPayload = true;
    }

    public TransformerException(String message, String topic, String kafkaPayload, Boolean dropPayload) {
        super(message);

        this.topic = topic;
        this.kafkaPayload = kafkaPayload;
        this.dropPayload = dropPayload;
    }
}
