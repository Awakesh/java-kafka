package co.lemnisk.data.migration;

import co.lemnisk.data.migration.model.KafkaPayload;
import co.lemnisk.data.migration.producer.KafkaProducerClient;
import co.lemnisk.utils.streamClient.helpers.EventHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EventSender implements EventHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventSender.class);
  protected final List<KafkaPayload> batch;
  private final LogAndMonitorPayload logAndMonitorPayload;

  public EventSender(final List<KafkaPayload> batch, LogAndMonitorPayload logAndMonitorPayload) {
    this.batch = batch;
    this.logAndMonitorPayload = logAndMonitorPayload;
  }

  @Override
  public void run() {
    processBatch();
    batch.clear();
  }

  private void processBatch() {
    for (final KafkaPayload kafkaPayload : batch) {
      try {
        KafkaProducerClient kafkaProducerClient = new KafkaProducerClient();
        kafkaProducerClient.sendMessage(kafkaPayload, logAndMonitorPayload);
      } catch (Exception e) {
        LOGGER.error("Error processing kafkaPayload {}", kafkaPayload, e);
      }
    }
  }
}
