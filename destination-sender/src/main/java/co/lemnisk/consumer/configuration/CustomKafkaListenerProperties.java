package co.lemnisk.consumer.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import co.lemnisk.consumer.model.CustomKafkaListenerProperty;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "custom.kafka")
public class CustomKafkaListenerProperties {
    private Map<String, CustomKafkaListenerProperty> listeners;
}
