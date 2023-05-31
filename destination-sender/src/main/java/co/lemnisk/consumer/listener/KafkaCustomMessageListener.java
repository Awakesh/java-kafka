package co.lemnisk.consumer.listener;

import co.lemnisk.common.logging.EventLogger;
import co.lemnisk.common.tracing.TraceHelper;
import co.lemnisk.common.tracing.Tracing;
//import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.tracing.TracingConstant;
import co.lemnisk.consumer.logger.DestinationReciever;
import co.lemnisk.consumer.util.TopicQueueRunner;
import co.lemnisk.consumer.utils.MessageQueue;
import co.lemnisk.consumer.utils.ParserUtils;
import io.opentelemetry.api.trace.Span;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpoint;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KafkaCustomMessageListener extends CustomMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCustomMessageListener.class);

    @Autowired
    Tracing tracing;

    String topic;

    @Override
    @SneakyThrows
    public KafkaListenerEndpoint createKafkaListenerEndpoint(String name, String topic) {
        this.topic = topic;
        MethodKafkaListenerEndpoint<String, String> kafkaListenerEndpoint = createDefaultMethodKafkaListenerEndpoint(name, topic);
        kafkaListenerEndpoint.setBean(new MyMessageListener());
        kafkaListenerEndpoint.setMethod(MyMessageListener.class.getMethod("onMessage", ConsumerRecord.class));
        return kafkaListenerEndpoint;
    }

    //@Slf4j
    private class MyMessageListener implements MessageListener<String, String> {
        @Override
        public void onMessage(ConsumerRecord<String, String> record) {
            Span span = TraceHelper.createSpan(tracing, record.headers(), TracingConstant.SpanNames.DESTINATION_SENDER, topic);
            try {
                addTracingAttributes(span, record.value());
                DestinationReciever.getInstance().log(record.topic(), EventLogger.MESSAGE_FLOW.INCOMING, record.value());
                MessageQueue.getInstance().setMessageQueue(record.topic(), record.value(), record.headers());
                TopicQueueRunner.getInstance();
            }
            // TODO: Add Catch block
            finally {
                TraceHelper.closeSpan(record.headers(), span);
            }
        }
    }

    private void addTracingAttributes(Span span, String value) {
        Map<String, String> attributesList = ParserUtils.getValues(value);
        TraceHelper.addSpanAttributes(span, attributesList.get("type"), attributesList.get("messageId"), attributesList.get("accountId"), Integer.valueOf(attributesList.get("destinationInstanceId")));
    }
}
