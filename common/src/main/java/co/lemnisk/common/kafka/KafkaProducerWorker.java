package co.lemnisk.common.kafka;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.kafka.util.RestUtil;
import co.lemnisk.common.kafka.util.TopicList;
import co.lemnisk.common.logging.DestTransformerOutputEventLogger;
import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.common.tracing.TracingConstant;
import io.opentelemetry.api.trace.Span;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class KafkaProducerWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerWorker.class);
    
    @Autowired
    @Qualifier("kafkaProducerTemplate")
    private KafkaTemplate<String, String> kafkaProducerTemplate;

    @Autowired
    private KafkaUtil kafkaUtil;

    @Autowired
    private RestUtil restUtil;

    @Value("${destination.topic.prefix}")
    private String topicNamePrefix;

    // Currently, key is not used
    public void sendMessageToKafka(String message, String topic, String key, Span jaegerSpan){
        Instant start = Instant.now();
        try {
            LOGGER.info("Message being sent to kafka topic: {} and message : {} ", topic, message);
            try {
                cacheCheckAndValidate(topic);
            }catch (Exception ex){
                throw new TransformerException("Error in caching, creating listener:" + ex ,topic,message,true);
            }
            DestTransformerOutputEventLogger.getInstance().log(topic, EventLogger.MESSAGE_FLOW.OUTGOING, message);

            ProducerRecord<String, String> prodRecord= new ProducerRecord<>(topic, message);

            LOGGER.info("Jaeger SpanID: {} \t TracerID: {}", jaegerSpan.getSpanContext().getSpanId(), jaegerSpan.getSpanContext().getTraceId());

            RecordHeader spanIdHeader = new RecordHeader(TracingConstant.SPAN_ID,
                jaegerSpan.getSpanContext().getSpanId().getBytes(StandardCharsets.UTF_8));

            RecordHeader tracerIdHeader = new RecordHeader(TracingConstant.TRACER_ID,
                jaegerSpan.getSpanContext().getTraceId().getBytes(StandardCharsets.UTF_8));

            prodRecord.headers().add(spanIdHeader);
            prodRecord.headers().add(tracerIdHeader);
            ListenableFuture<SendResult<String, String>> send = kafkaProducerTemplate.send(prodRecord);

            send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                @Override
                public void onSuccess(SendResult<String, String> result) {
                    LOGGER.info("Successfully sent the message to kafka : {}, metadata:{}", result.getProducerRecord(), result.getRecordMetadata());
                }

                @SneakyThrows
                @Override
                public void onFailure(Throwable ex) {
                    LOGGER.error("cause : {}   , Error Message :{},message:{}", ex.getCause(), ex.getMessage(), message);
                    kafkaUtil.createNewTopic(topic);
                    if(TopicList.getInstance().getTopics().size() < 1){
                        Set<String> topicSet = kafkaUtil.getTopicList();
                        List<String> topicList = topicSet.stream().collect(Collectors.toList());
                        TopicList.getInstance().setTopicsList(topicList);
                    }
                    restUtil.post(topic);
                    TopicList.getInstance().setTopic(topic);
                    sendMessageToKafka(message, topic, key, jaegerSpan);
                }
            });

        } catch(Exception exception) {
            throw new TransformerException("Exception occurred while sending message to kafka :" + exception.getMessage(),topic,message,true);

        }
        LOGGER.info("Time taken in send message to kafka : {}", Instant.now().toEpochMilli() - start.toEpochMilli());
    }

    private void cacheCheckAndValidate(String topic) throws IOException, InterruptedException {
        List<String> topics = new ArrayList<>();
        try {
            topics = TopicList.getInstance().getTopics();

            // TODO: Move this to a separate method
            topics = topics.stream()
                    .filter(t -> (t.startsWith(topicNamePrefix) && !t.endsWith(".DLT")))
                    .collect(Collectors.toList());
        }catch (Exception ex){
            throw new TransformerException("Error in getting topics");
        }

        if(topics.isEmpty()) {
            topics = new ArrayList<>(kafkaUtil.getTopicList());

            // TODO: Move this to a separate method
            topics = topics.stream()
                    .filter(t -> (t.startsWith(topicNamePrefix) && !t.endsWith(".DLT")))
                    .collect(Collectors.toList());
        }

        if(topics.isEmpty() || ( topics.size() > 0 && !topics.contains(topic))) {
            try {
                kafkaUtil.createNewTopic(topic);
                LOGGER.info("New Topic created with name {} ", topic);
                kafkaUtil.createNewTopic(topic + "." + Constants.DLT);
                LOGGER.info("New DLT Topic created with name {} ", topic + "." + Constants.DLT);
            }catch (Exception ex){
                // TODO: Handle this efficiently
//                throw new TransformerException("Error creating topic:" + ex.getMessage());
            }

            try {

                LOGGER.info(">>>> API Call to topic");
                String response = restUtil.post(topic);
                LOGGER.info("Message from consumer : " + response);
            }catch(Exception ex){
                throw new TransformerException("Error on invoking topic : " + ex.getMessage());
            }

            TopicList.getInstance().setTopicConsumer(topic);
        }
        List<String> consumerList = new ArrayList<>();
        try {
            consumerList = TopicList.getInstance().getTopicsConsumer();
        }catch (Exception ex){
            throw new TransformerException("Error getting the Consumer topics." + ex.getMessage());
        }

        // TODO: Move this to a separate method
        consumerList = consumerList.stream()
                .filter(t -> (t.startsWith(topicNamePrefix) && !t.endsWith(".DLT")))
                .collect(Collectors.toList());

        topics.stream().forEach(topicVar->{
            try {
                if((topicVar.startsWith(topicNamePrefix) && !topicVar.endsWith(".DLT")) && !TopicList.getInstance().getTopicsConsumer().contains(topicVar)) {
                    try {
                        String json = restUtil.post(topicVar);
                    }catch (Exception ex){
                        throw new TransformerException("Error in calling Consumer listener client call" + ex.getMessage());
                    }
                    TopicList.getInstance().setTopicConsumer(topicVar);
                }
            } catch (IOException e) {
                throw new TransformerException("Error in consumer listener api call:" + e.getStackTrace());
            }
        });
    }
}