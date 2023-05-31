package co.lemnisk.data.migration;

import co.lemnisk.utils.streamClient.consumers.StreamConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerStarter implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerStarter.class);

    @Autowired
    private StreamConsumer streamConsumer;

    @Override
    public void run(String... arg0) throws Exception {
        LOGGER.info("Kafka Consumer is starting...");
        streamConsumer.execute();
    }
}
