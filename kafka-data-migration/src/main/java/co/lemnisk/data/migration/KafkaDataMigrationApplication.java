package co.lemnisk.data.migration;

import co.lemnisk.utils.streamClient.consumers.StreamConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class KafkaDataMigrationApplication { //implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDataMigrationApplication.class);

	@Autowired
	private StreamConsumer streamConsumer;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(KafkaDataMigrationApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

//	@Override
//	public void run(String... arg0) throws Exception {
//		LOGGER.info("Kafka Consumer is starting...");
//		streamConsumer.execute();
//	}

}

