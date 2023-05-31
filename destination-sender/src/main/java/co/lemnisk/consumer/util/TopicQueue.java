package co.lemnisk.consumer.util;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.lemnisk.consumer.dto.TopicDetails;

public class TopicQueue {
	private static final Logger logger = LoggerFactory.getLogger(TopicQueue.class);

	private static TopicQueue INSTANCE = null;
	private static List<TopicDetails> topicList= null;
	private static List<String> consumerTopicList= null;

	private TopicQueue() {

	}

	public static TopicQueue getInstance() {
		if (INSTANCE == null) {
			synchronized (TopicQueue.class) {
				if (INSTANCE == null) {
					INSTANCE = new TopicQueue();
					topicList = new LinkedList<TopicDetails>();
				}
			}
		}
		return INSTANCE;
	}

	public boolean setTopic(TopicDetails topicDetails) {
		return topicList.add(topicDetails);
	}
	
	public List<TopicDetails> getTopics(){
		return topicList;		
	}
	
	public boolean removeTopic(String topicName){
		return true;		
	}
	
	public boolean addNewTopicDetails(String topicName) {
		logger.info("New topic {} added to the list", topicName);
		try {
			TopicDetails topicDetails = new TopicDetails();
			topicDetails.setTopicName(topicName);
			topicDetails.setCreatedTime(DateTimeUtil.getDateTimeInMilliseconds());
			topicDetails.setTopicStatus(Constants.Status.CREATED.name());
			topicList.add(topicDetails);
		}catch(Exception ex) {
			logger.error("Exeption in adding new topic.");
			return false;
		}

		return true;
	}
	public void setTopic(String topic){
		consumerTopicList.add(topic);
	}

	public List<String> getTopicList(){
		return consumerTopicList;
	}
}// end of the class

