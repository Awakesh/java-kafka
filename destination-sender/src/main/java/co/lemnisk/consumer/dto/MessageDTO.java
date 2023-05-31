package co.lemnisk.consumer.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.header.Headers;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO implements Serializable {
	private String topicName;
	private String message;
	private Headers headers;
}
