package co.lemnisk.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import co.lemnisk.consumer.listener.CustomKafkaListenerRegistrar;
import co.lemnisk.consumer.model.CustomKafkaListenerProperty;

@Service
public class KafkaConsumerService {

	@Autowired
	private CustomKafkaListenerRegistrar customKafkaListenerRegistrar;

	@Async("asyncExecutor")
	public void kafkaListnerRegistry(String topic, String listenerClass, Boolean startImmediately) {
		customKafkaListenerRegistrar.registerCustomKafkaListener(null,
				CustomKafkaListenerProperty.builder().topic(topic).listenerClass(listenerClass).build(),
				startImmediately);		
	}

}
