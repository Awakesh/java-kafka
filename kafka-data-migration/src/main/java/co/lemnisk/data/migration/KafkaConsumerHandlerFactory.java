package co.lemnisk.data.migration;

import co.lemnisk.data.migration.model.KafkaPayload;
import co.lemnisk.utils.streamClient.executor.ExecutorServiceWrapper;
import co.lemnisk.utils.streamClient.handlers.StreamConsumerHandler;
import co.lemnisk.utils.streamClient.handlers.StreamConsumerHandlerFactory;
import co.lemnisk.utils.streamClient.helpers.EventHelper;
import co.lemnisk.utils.streamClient.streams.KafkaMessageStream;
import co.lemnisk.utils.streamClient.streams.MessageStream;
import kafka.consumer.KafkaStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("kafkaConsumerHandlerFactory")
public class KafkaConsumerHandlerFactory implements StreamConsumerHandlerFactory<KafkaStream<byte[], byte[]>> {

    @Autowired
    private ExecutorServiceWrapper executorServiceWrapper;

    @Autowired
    private LogAndMonitorPayload logAndMonitorPayload;

    @Autowired
    private CampaignFilter campaignFilter;

    @Value("#{${input.output.topic.mapping}}")
    private Map<String, String> topicMapping;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerHandlerFactory.class);

    @Override
    public StreamConsumerHandler getStreamConsumerHandler(
            final MessageStream<KafkaStream<byte[], byte[]>> messageStream,
            final Map<String, Object> consumerHandlerConfig, final int threadNumber) {
        try {
            return new CustomKafkaEventHandler(messageStream, consumerHandlerConfig,
                    threadNumber, executorServiceWrapper, logAndMonitorPayload, topicMapping, campaignFilter) {

                @Override
                protected EventHelper getEventHelper(List<KafkaPayload> kafkaPayloadList) {
                    return new EventSender(kafkaPayloadList, logAndMonitorPayload);
                }
            };
        } catch (Exception ex) {
            LOGGER.error("Error while creating event handler instance.");
            return null;
        }
    }

    @Override
    public MessageStream<KafkaStream<byte[], byte[]>> getMessageStream() {
        return new KafkaMessageStream();
    }
}
