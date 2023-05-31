
package co.lemnisk.consumer.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.util.Optional;

public abstract class CustomMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomMessageListener.class);
	private static int NUMBER_OF_LISTENERS = 0;

	@Value("${kafka.consumer.group.prefix}")
	public String consumerGroupPrefix;

	@Value("${kafka.consumer.concurrency}")
	public Integer consumerConcurrency;

	public abstract KafkaListenerEndpoint createKafkaListenerEndpoint(String name, String topic);

	protected MethodKafkaListenerEndpoint<String, String> createDefaultMethodKafkaListenerEndpoint(String name, String topic) {
		LOGGER.info("Create Listener for consumer id : {} and topic {} and Listener {}", name, topic, NUMBER_OF_LISTENERS);
		MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint = new MethodKafkaListenerEndpoint<>();
		kafkaListenerEndpoint.setId(getConsumerId(name));
		kafkaListenerEndpoint.setGroupId(consumerGroupPrefix + topic);
		kafkaListenerEndpoint.setConcurrency(consumerConcurrency);
		kafkaListenerEndpoint.setAutoStartup(true);
		kafkaListenerEndpoint.setTopics(topic);
		kafkaListenerEndpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
		return kafkaListenerEndpoint;
	}

	private String getConsumerId(String name) {
		if (isBlank(name)) {
			return "ak=" + CustomMessageListener.class.getCanonicalName() + "#" + NUMBER_OF_LISTENERS++;
		} else {
			return name;
		}
	}

	private boolean isBlank(String string) {
		return Optional.ofNullable(string).map(String::isBlank).orElse(true);
	}
}
