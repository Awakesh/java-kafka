package co.lemnisk.consumer.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class TopicListenerDTO implements Serializable{
	public boolean status;
	private String message;
}
