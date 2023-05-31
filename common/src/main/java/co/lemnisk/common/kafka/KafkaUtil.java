package co.lemnisk.common.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.kafka.configuration.ProducerConfiguration;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class KafkaUtil {
	private static Logger LOGGER = LogManager.getLogger(KafkaUtil.class);
	@Value("${common.config.kafka.topic.replication}")
	private String replication;

	@Value("${common.config.kafka.topic.partition}")
	private String partition;

	public Set<String> getTopicList() {
		AdminClient adminClient = AdminClient.create(ProducerConfiguration.config());
		ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
		listTopicsOptions.listInternal(true);
		Set<String> topicSet = null;
		try {
			LOGGER.info("Topic names: {}", adminClient.listTopics(listTopicsOptions).names().get());
			topicSet = adminClient.listTopics(listTopicsOptions).names().get();
		} catch (InterruptedException | ExecutionException e) {
			throw new TransformerException("Exception while fetching topic list: " + e.getMessage() );
		}
		return topicSet;
	}

	public boolean createNewTopic(String topicName) {
		try {
			if(partition == null || partition.length() < 1){
				partition = "1";
			}
			if(replication == null || replication.length() < 1){
				replication = "3";
			}

			LOGGER.info("Partition : {}",partition);
			LOGGER.info("Replication : {}",replication);
			LOGGER.info("Topic Name: {}", topicName);
			AdminClient adminClient = AdminClient.create(ProducerConfiguration.config());
			NewTopic topic = new NewTopic(topicName, Integer.parseInt(partition), Short.valueOf(replication));
			List<NewTopic> topics = Arrays.asList(topic);
			CreateTopicsResult newTopic = adminClient.createTopics(topics);
			KafkaFuture<Void> future = newTopic.values().get(topicName);
			future.get();
			LOGGER.info("New topic created : {}", newTopic);
		} catch (Exception ex) {
			throw new TransformerException("Error in creating new topic :" + ex.getMessage());
		}
		return true;
	}
}
