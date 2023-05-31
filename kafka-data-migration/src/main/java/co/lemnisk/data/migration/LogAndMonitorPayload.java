package co.lemnisk.data.migration;

import co.lemnisk.data.migration.constants.DataMigrationConstant;
import co.lemnisk.data.migration.metrics.EventMonitoring;
import co.lemnisk.data.migration.model.KafkaPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogAndMonitorPayload {

    Logger logger = LoggerFactory.getLogger(LogAndMonitorPayload.class.getName());

    @Autowired
    private EventMonitoring eventMonitoring;

    public void handleIncomingPayload(KafkaPayload kafkaPayload) {
        eventMonitoring.addPayloadMetric(kafkaPayload.getInputTopic(), kafkaPayload.getOutputTopic(), DataMigrationConstant.PayloadType.INCOMING);
        logger.info("{} - {}", DataMigrationConstant.PayloadType.INCOMING, kafkaPayload);
    }

    public void handleOutgoingPayload(KafkaPayload kafkaPayload) {
        eventMonitoring.addPayloadMetric(kafkaPayload.getInputTopic(), kafkaPayload.getOutputTopic(), DataMigrationConstant.PayloadType.OUTGOING);
        logger.info("{} - {}", DataMigrationConstant.PayloadType.OUTGOING, kafkaPayload);
    }
}
