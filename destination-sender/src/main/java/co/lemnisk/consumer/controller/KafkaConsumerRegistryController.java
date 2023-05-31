package co.lemnisk.consumer.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.lemnisk.consumer.dto.TopicListenerDTO;
import co.lemnisk.consumer.util.Constants;
import co.lemnisk.consumer.util.TopicQueue;
import co.lemnisk.consumer.listener.CustomKafkaListenerRegistrar;
import co.lemnisk.consumer.model.KafkaConsumerAssignmentResponse;
import co.lemnisk.consumer.model.KafkaConsumerResponse;
import co.lemnisk.consumer.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/kafka")
public class KafkaConsumerRegistryController {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerRegistryController.class);
	
	@Autowired
	private KafkaConsumerService kafkaConsumerService;
	
    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private CustomKafkaListenerRegistrar customKafkaListenerRegistrar;

    @GetMapping(path = "/getConsumerIds")
    public List<KafkaConsumerResponse> getConsumerIds() {
        logger.info("Fetchinggg consumer Ids...");

        return kafkaListenerEndpointRegistry.getListenerContainerIds()
                .stream()
                .map(this::createKafkaConsumerResponse)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createConsumer(@RequestParam(required = true) String topicName) {
    	logger.info("Startinggg topic name {}", topicName);
    	
    	TopicListenerDTO topicListenerDTO = new TopicListenerDTO();
    	HttpHeaders headers = new HttpHeaders();
        
    	try {    		
    		
	    	if(TopicQueue.getInstance() != null)
	    		TopicQueue.getInstance().addNewTopicDetails(topicName);
	    	String listenerClass = null;
	    	
	    	if(listenerClass == null || listenerClass.isBlank()) {
	    		listenerClass = Constants.DEFAULT_LISTENER;
	    	}
	    	
	    	kafkaConsumerService.kafkaListnerRegistry(topicName, listenerClass, true);
	    	topicListenerDTO.setStatus(true);
	    	topicListenerDTO.setMessage("Listener Added");
	    	headers.add("Responded", "MyController");
    	}catch(Exception ex) {
    		logger.error("Problem in creating listener for topic {}", topicName);
    		topicListenerDTO.setStatus(false);
	    	topicListenerDTO.setMessage("Unable to add Listener");
    	}
		return ResponseEntity.status(HttpStatus.CREATED).body(topicListenerDTO);
    }

    @PostMapping(path = "/activate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (listenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is already running", consumerId));
        } else {
            log.info("Running a consumer with id " + consumerId);
            listenerContainer.start();
        }
    }

    @PostMapping(path = "/pause")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void pauseConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!listenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is not running", consumerId));
        } else if (listenerContainer.isContainerPaused()) {
            throw new RuntimeException(String.format("Consumer with id %s is already paused", consumerId));
        } else if (listenerContainer.isPauseRequested()) {
            throw new RuntimeException(String.format("Consumer with id %s is already requested to be paused", consumerId));
        } else {
            log.info("Pausing a consumer with id " + consumerId);
            listenerContainer.pause();
        }
    }

    @PostMapping(path = "/resume")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resumeConsumer(@RequestParam String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!listenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is not running", consumerId));
        } else if (!listenerContainer.isContainerPaused()) {
            throw new RuntimeException(String.format("Consumer with id %s is not paused", consumerId));
        } else {
            log.info("Resuming a consumer with id " + consumerId);
            listenerContainer.resume();
        }
    }

    @PostMapping(path = "/deactivate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deactivateConsumer(@RequestParam(value = "consumerId") String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new RuntimeException(String.format("test Consumer with id %s is not found", consumerId));
        } else if (!listenerContainer.isRunning()) {
            throw new RuntimeException(String.format("test Consumer with id %s is already stop", consumerId));
        } else {
            log.info("Stopping a consumer with id " + consumerId);
            listenerContainer.stop();
        }
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
}
