package co.lemnisk.common.kafka.util;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.kafka.KafkaProducerWorker;
import co.lemnisk.common.metrics.EventMonitoring;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import scala.collection.immutable.Stream;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageParser.class);

    @Value("${destination.topic.prefix}")
    private String topicNamePrefix;

    @Value("${common.config.kafka.topic.key}")
    private String topicKey;

    @Autowired
    KafkaProducerWorker kafkaProducerWorker;

    @Autowired
    EventMonitoring eventMonitoring;

    @Async("asyncExecutor")
    public void parseJson(String json, Span jaegerSpan) throws JsonProcessingException {
        LOGGER.info("Received Json at parseJson for Processing as :  {}", json);
        String campaignId = "";
        String destinationInstanceId = "";

        Map<CharSequence, Object> propertiesMap = new HashMap<>();

        if(json != null && json.length() > 0) {

            campaignId = JsonPath.parse(json).read(Constants.CONTEXT_ACCOUNT_ID, String.class);
            LOGGER.info("campaignId :  {}", campaignId);
            destinationInstanceId = JsonPath.parse(json).read(Constants.DESTINATION_INSTANCE_ID, String.class);
            LOGGER.info("destinationInstanceId :  {}", destinationInstanceId);

            if(campaignId == null || campaignId.length() < 1){
               throw new TransformerException("campaignId is blank or null : " + campaignId);
            }

            if(destinationInstanceId == null || destinationInstanceId.length() < 1){
               throw new TransformerException("destinationInstanceId is blank or null:" + destinationInstanceId);
            }
            String topic = topicNamePrefix + "-" + campaignId +"-" + destinationInstanceId;
            LOGGER.info("TOPIC name is : {}", topic);

            kafkaProducerWorker.sendMessageToKafka(json, topic, topicKey, jaegerSpan);
        }

    }
}
