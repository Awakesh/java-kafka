package co.lemnisk.consumer.configuration;


import co.lemnisk.common.constants.Constants;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class KafkaPropertiesConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPropertiesConfiguration.class);

    public static Properties config() {

        Properties config = new Properties();
        try {
            LOGGER.info("Loading input file from {}" , Constants.CONFLUENT_CONFIG_PATH);
            try (FileInputStream fileInputStream = new FileInputStream(Constants.CONFLUENT_CONFIG_PATH)) {
                config.load(fileInputStream);
                LOGGER.info("Properties file loaded");
            }
        }
        catch (Exception ex) {
            LOGGER.error("Exception while loading confluent properties");
        }
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return config;
    }
}