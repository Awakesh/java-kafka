package co.lemnisk.consumer.utils;

import co.lemnisk.consumer.configuration.KafkaPropertiesConfiguration;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class KafkaUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaUtils.class);

    private static final int DELETE_TIMEOUT_SECONDS = 10;
    private AdminClient adminClient = null;

    public KafkaUtils() {
        adminClient = AdminClient.create(KafkaPropertiesConfiguration.config());
    }

    public void deleteTopic(String topicName) throws Exception {
        if (topicName != null && adminClient.listTopics().names().get(DELETE_TIMEOUT_SECONDS, TimeUnit.SECONDS).contains(topicName)) {
            try {
                adminClient.deleteTopics(Collections.singleton(topicName)).all().get(DELETE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                LOGGER.error("Did not receive delete topic response within %d seconds. Checking if it succeeded", DELETE_TIMEOUT_SECONDS);
                if (adminClient.listTopics().names().get(DELETE_TIMEOUT_SECONDS, TimeUnit.SECONDS).contains(topicName)) {
                    throw new Exception("Topic still exists after timeout");
                }
            }
        }else {
            LOGGER.info("Either topic value is null or the topic : {} does not exist in the kafka cluster", topicName);
        }
    }

    public void describeTopic(String topicName) throws Exception {

        if (topicName != null && adminClient.listTopics().names().get(DELETE_TIMEOUT_SECONDS, TimeUnit.SECONDS).contains(topicName)) {
            try {

                ConfigResource resource = new ConfigResource(ConfigResource.Type.TOPIC, topicName);
                DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Collections.singleton(resource));
                Map<ConfigResource, Config> config;
                config = describeConfigsResult.all().get();
                ConfigEntry retentionEntry = new ConfigEntry(TopicConfig.RETENTION_MS_CONFIG, "60000");
                Map<ConfigResource, Config> updateConfig = new HashMap<ConfigResource, Config>();
                updateConfig.put(resource, new Config(Collections.singleton(retentionEntry)));

                AlterConfigsResult alterConfigsResult = adminClient.alterConfigs(updateConfig);
                alterConfigsResult.all();

                describeConfigsResult = adminClient.describeConfigs(Collections.singleton(resource));

                config = describeConfigsResult.all().get();
            } catch (TimeoutException e) {
                LOGGER.error("The topic {} does not exist.", topicName);
            }
        }else {
            LOGGER.error("Topic name is blank or it does not exist in the kafka cluster" + topicName);
        }

    }

    public Set<String> getTopicList() {
        LOGGER.info("called getTopicList");
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);
        Set<String> topicSet = null;
        try {
            topicSet = adminClient.listTopics(listTopicsOptions).names().get();
            LOGGER.info("Topic Names getTopicList {} ", topicSet);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            LOGGER.error("Error in getting topic list {}", e);
        }
        return topicSet;
    }
}

