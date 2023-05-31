package co.lemnisk.consumer.utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import co.lemnisk.consumer.dto.MessageDTO;
import co.lemnisk.consumer.util.Constants;
import co.lemnisk.consumer.util.TopicQueue;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageQueue.class);

    private static MessageQueue INSTANCE = null;
    private static Queue<MessageDTO> messageQueue = null;

    private MessageQueue() {

    }

    public static MessageQueue getInstance() {
        if (INSTANCE == null) {
            synchronized (TopicQueue.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageQueue();
                    messageQueue = new LinkedBlockingQueue<MessageDTO>();
                }
            }
        }
        return INSTANCE;
    }

    public boolean setMessageQueue(String topic, String message, Headers header) {
        if (topic != null && message != null) {
            try {
                MessageDTO messageDTO = new MessageDTO(topic, message, header);
               messageQueue.add(messageDTO);
            } catch (Exception exception) {
                LOGGER.error("Exception in adding queue.");
                return false;
            }
        }

        return true;
    }

    public MessageDTO getMessageQueue(){
        return messageQueue.poll();
    }

}
