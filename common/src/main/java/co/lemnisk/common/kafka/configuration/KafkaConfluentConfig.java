package co.lemnisk.common.kafka.configuration;

import co.lemnisk.common.constants.Constants;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.subject.RecordNameStrategy;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KafkaConfluentConfig {

    private KafkaConfluentConfig() {}

    public static Properties streamConfig() throws IOException {

        Properties config = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(Constants.CONFLUENT_CONFIG_PATH)) {
            config.load(fileInputStream);
        }

        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);
        config.put(AbstractKafkaSchemaSerDeConfig.VALUE_SUBJECT_NAME_STRATEGY, RecordNameStrategy.class.getName());
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,1);
        config.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG,600000);
        return config;
    }

    public static Properties config() throws IOException {

        Properties config = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(Constants.CONFLUENT_CONFIG_PATH)) {
            config.load(fileInputStream);
        }

        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return config;
    }
}
