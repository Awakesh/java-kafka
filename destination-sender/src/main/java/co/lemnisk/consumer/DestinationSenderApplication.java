package co.lemnisk.consumer;

import co.lemnisk.common.service.DestinationInstanceMappingService;
import co.lemnisk.consumer.model.KafkaConsumerAssignmentResponse;
import co.lemnisk.consumer.model.KafkaConsumerResponse;
import co.lemnisk.consumer.service.KafkaConsumerService;
import co.lemnisk.consumer.util.Constants;
import co.lemnisk.consumer.utils.KafkaUtils;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@EnableKafka
@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(value = "co.lemnisk.*")
@EnableJpaRepositories(value = "co.lemnisk.*")
@EntityScan(value = "co.lemnisk.*")
@EnableCaching
@EnableScheduling
public class DestinationSenderApplication implements CommandLineRunner {
	private static Logger LOGGER = LoggerFactory.getLogger(DestinationSenderApplication.class);
	@Autowired
	private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
	@Autowired
	private KafkaConsumerService kafkaConsumerService;

	@Autowired
	DestinationInstanceMappingService destinationInstanceService;

	@Value("${destination.topic.prefix}")
	private String topicNamePrefix;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DestinationSenderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		queryToCache();
		String listenerClass = Constants.DEFAULT_LISTENER;
		List<KafkaConsumerResponse> consumerResponseList= kafkaListenerEndpointRegistry.getListenerContainerIds()
				.stream()
				.map(this::createKafkaConsumerResponse)
				.collect(Collectors.toList());
		List<String> topicListActive = consumerResponseList.stream().flatMap(k-> k.getAssignments().stream()).map(KafkaConsumerAssignmentResponse::getTopic).collect(Collectors.toList());

		KafkaUtils kafkaUtils = new KafkaUtils();
		Set<String> topicSet = kafkaUtils.getTopicList();
		List<String> topicList = new ArrayList<>(topicSet);

		// TODO: Move this to a separate method
		topicList.stream()
				.filter(t -> (t.startsWith(topicNamePrefix) && !t.endsWith(".DLT")))
				.forEach(topic -> {
			kafkaConsumerService.kafkaListnerRegistry(topic, listenerClass, true);
			LOGGER.info("Topic listener started for the topic name : {} ",topic);
		});

	}

	private KafkaConsumerResponse createKafkaConsumerResponse(String consumerId) {
		MessageListenerContainer listenerContainer =
				kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
		return KafkaConsumerResponse.builder()
				.consumerId(consumerId)
				.groupId(listenerContainer.getGroupId())
				.listenerId(listenerContainer.getListenerId())
				.active(listenerContainer.isRunning())
				.assignments(Optional.ofNullable(listenerContainer.getAssignedPartitions())
						.map(topicPartitions -> topicPartitions.stream()
								.map(this::createKafkaConsumerAssignmentResponse)
								.collect(Collectors.toList()))
						.orElse(null))
				.build();
	}

	private KafkaConsumerAssignmentResponse createKafkaConsumerAssignmentResponse(
			TopicPartition topicPartition) {
		return KafkaConsumerAssignmentResponse.builder()
				.topic(topicPartition.topic())
				.partition(topicPartition.partition())
				.build();
	}

	private void queryToCache() {
		destinationInstanceService.getAllActiveDestinationInstances();
	}
}
