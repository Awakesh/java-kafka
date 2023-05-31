package co.lemnisk.common.kafka.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.kafka.KafkaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;

@Component
public class TopicList {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicList.class);

    private static TopicList INSTANCE = null;
    private static List<String> topicList = null;
    private static List<String> topicListConsumer = null;

    @Autowired
    static RestUtil restUtil;

    private TopicList() {

    }

    public static TopicList getInstance() throws IOException {
        if (INSTANCE == null) {
            synchronized (TopicList.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TopicList();
                    KafkaUtil ku = new KafkaUtil();
                    topicList = Collections.synchronizedList(new ArrayList<String>(ku.getTopicList()));
                    restUtil = new RestUtil();
                    topicListConsumer = Collections.synchronizedList(new ArrayList<String>());
                    topicListConsumer = Collections.synchronizedList(new ArrayList<>(getTopicList(restUtil.postRequest(restUtil.getUrl()))));
                }
            }
        }
        return INSTANCE;
    }

    public boolean setTopic(String topicName) {
        return topicList.add(topicName);
    }

    public List<String> getTopics(){
        return topicList;
    }

    public List<String> setTopicsList(List<String> topics){
        return this.topicList = topics;
    }

    public boolean setTopicConsumer(String topicName) {
        try{
            topicListConsumer.add(topicName);
        }catch (Exception e){
            LOGGER.error("Error in adding topic {}", e);
            return false;
        }
        return true;
    }

    public List<String> getTopicsConsumer(){
        try {
            if (topicListConsumer.size() > 0) {
                return topicListConsumer;
            }
        }catch (Exception e){
            throw new TransformerException("Error in getting consumers topic:" + e.getMessage());
        }
        return Collections.synchronizedList(new ArrayList<String>());
    }

    public List<String> setTopicsListConsumer(List<String> topics){
        return this.topicListConsumer = topics;
    }

    private static List<String> getTopicList(String json){
        LOGGER.info("Get topics from json {}", json);
        String[] topicArr = JsonPath.parse(json).read("$.[*].assignments[*].topic", String[].class);
        List<String> topicList = Arrays.asList(topicArr);

        topicList
            .stream()
            .distinct()
            .forEach(topic -> {
                try {
                    LOGGER.info("Calling Listener API for adding new topic to listener.");
                    restUtil.post(topic);
                    LOGGER.info("Successfully added the topic {} to listener.", topic);
                } catch (IOException e) {
                    throw new TransformerException("Error in adding new topic" + topic + "to listener. Due to exception" + e.getStackTrace());
                }
            });
        return topicList;
    }

}// end of the class

