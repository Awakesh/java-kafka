package co.lemnisk.data.migration.model;

import lombok.Data;

@Data
public class KafkaPayload {
    private String key;
    private String inputTopic;
    private String outputTopic;
    private String value;
}
