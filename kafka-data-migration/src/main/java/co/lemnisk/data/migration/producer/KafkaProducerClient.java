package co.lemnisk.data.migration.producer;

import co.lemnisk.data.migration.LogAndMonitorPayload;
import co.lemnisk.data.migration.constants.DataMigrationConstant;
import co.lemnisk.data.migration.model.KafkaPayload;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class KafkaProducerClient {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerClient.class.getName());

    private static final KafkaProducer<String, String> producer = initProducer();

    public void sendMessage(KafkaPayload kafkaPayload, LogAndMonitorPayload logAndMonitorPayload) {

        String topicName = kafkaPayload.getOutputTopic();
        String key = kafkaPayload.getKey();
        String value = kafkaPayload.getValue();

        if (value.isEmpty())
            return;

        if (producer == null) {
            logger.error("Producer configuration is not found");
            return;
        }

        logAndMonitorPayload.handleOutgoingPayload(kafkaPayload);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, key, value);

        producer.send(producerRecord);
    }

    private static KafkaProducer<String, String> initProducer() {

        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(DataMigrationConstant.CONFLUENT_CONFIG_PATH)) {
            properties.load(fileInputStream);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);

        return new KafkaProducer<>(properties);
    }
}
