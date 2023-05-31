package co.lemnisk.consumer.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TopicDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String topicName;
	//private String consumerGroupId;
	private long createdTime;
	private long lastRecordUpdated;
	//private long lastUpdatedTime;
	private long deletedTime;
	private String topicStatus;
}
