package co.lemnisk.common.kafka.configuration;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.exception.TransformerException;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class ProducerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerConfiguration.class);

    public static Properties config() {
        Properties config = new Properties();
        try {
            try (FileInputStream fileInputStream = new FileInputStream(Constants.CONFLUENT_CONFIG_PATH)) {
                config.load(fileInputStream);
            }
        }
        catch (Exception ex) {
            throw new TransformerException("Exception while loading confluent properties");
        }
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return config;
    }
}
