package co.lemnisk.data.migration;

import co.lemnisk.data.migration.model.KafkaPayload;
import co.lemnisk.utils.constants.StreamConstant;
import co.lemnisk.utils.constants.StreamConstant.CONSUMER_HANDLER_KEYS;
import co.lemnisk.utils.streamClient.executor.ExecutorServiceWrapper;
import co.lemnisk.utils.streamClient.handlers.StreamConsumerHandler;
import co.lemnisk.utils.streamClient.helpers.EventHelper;
import co.lemnisk.utils.streamClient.streams.MessageStream;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CustomKafkaEventHandler implements StreamConsumerHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomKafkaEventHandler.class);
  private static final DateTimeZone dateTimeZone = DateTimeZone
      .forID(StreamConstant.GMT_TIMEZONE);

  private final MessageStream<KafkaStream<byte[], byte[]>> messageStream;
  private final Map<String, Object> consumerHandlerConfig;
  private final int threadNumber;
  private final ExecutorServiceWrapper executorServiceWrapper;
  private final Map<String, String> topicMapping;
  private final LogAndMonitorPayload logAndMonitorPayload;
  private final CampaignFilter campaignFilter;

  private int maxBatchLag;
  private EventConsumerBatch[] consumerBatches;
  private List<KafkaPayload> batch;
  private int batchIndex;
  private int maxRetries;
  private int maxExecuteRetries;
  private int batchSize;
  private int batchTimeout;
  private long batchStartTimeInMs;
  private String threadName;
  private String topic;
  private ScheduledExecutorService scheduledExecutorService;
  private final Boolean batchSync = false;


  public CustomKafkaEventHandler(
      final MessageStream<KafkaStream<byte[], byte[]>> messageStream,
      final Map<String, Object> consumerHandlerConfig, final int threadNumber,
      final ExecutorServiceWrapper executorServiceWrapper,
      LogAndMonitorPayload logAndMonitorPayload, Map<String, String> topicMapping,
      CampaignFilter campaignFilter) {
    this.messageStream = messageStream;
    this.consumerHandlerConfig = Collections.unmodifiableMap(consumerHandlerConfig);
    this.threadNumber = threadNumber;
    this.executorServiceWrapper = executorServiceWrapper;
    this.topicMapping = topicMapping;
    this.logAndMonitorPayload = logAndMonitorPayload;
    this.campaignFilter = campaignFilter;
    init();
  }

  private void init() {
    maxBatchLag = (Integer) consumerHandlerConfig.get(CONSUMER_HANDLER_KEYS.BATCH_LAG.getValue());
    maxExecuteRetries = (Integer) consumerHandlerConfig
        .get(CONSUMER_HANDLER_KEYS.BATCH_RETRIES.getValue());
    batchSize = (Integer) consumerHandlerConfig.get(CONSUMER_HANDLER_KEYS.BATCH_SIZE.getValue());
    batchTimeout = (Integer) consumerHandlerConfig
        .get(CONSUMER_HANDLER_KEYS.BATCH_TIMEOUT.getValue());
    topic = (String) consumerHandlerConfig.get(CONSUMER_HANDLER_KEYS.TOPIC.getValue());
    threadName = "Kafka-" + topic + "-Consumer-" + threadNumber;
    consumerBatches = new EventConsumerBatch[maxBatchLag];
    for (int i = 0; i < maxBatchLag; i++) {
      consumerBatches[i] = new EventConsumerBatch(new ArrayList<>(batchSize), null);
    }
    final int batchTriggerTimeout = (Integer) consumerHandlerConfig
        .get(CONSUMER_HANDLER_KEYS.BATCH_TRIGGER_TIMEOUT.getValue());
    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    scheduledExecutorService
        .scheduleWithFixedDelay(this::clearBatch, batchTriggerTimeout, batchTriggerTimeout,
            TimeUnit.MILLISECONDS);
  }

  private void clearBatch() {
    synchronized (batchSync) {
      flushBatch();
    }
  }

  @Override
  public void shutDown() {
    if (Objects.nonNull(scheduledExecutorService)) {
      scheduledExecutorService.shutdown();
      LOGGER.error("killing the thread for batch time out in real time {}", threadName);
    }
  }

  @Override
  public void run() {
    LOGGER.info("Starting thread: {}", threadName);
    batchIndex = 0;
    batchStartTimeInMs = new DateTime(dateTimeZone).getMillis();
    long currentTimeInMs;
    boolean batchTimedOut;
    batch = new ArrayList<>(batchSize);
    maxRetries = maxExecuteRetries;
    final KafkaStream<byte[], byte[]> stream = messageStream.getStream();
    final ConsumerIterator<byte[], byte[]> it = stream.iterator();

    while (it.hasNext()) {
      KafkaPayload kafkaPayload = new KafkaPayload();

      MessageAndMetadata<byte[], byte[]> data = it.next();

      byte[] keyBytes = data.key();

      String key;

      if (keyBytes == null)
        key = UUID.randomUUID().toString();
      else
        key = new String(keyBytes, StandardCharsets.UTF_8);

      String line = new String(data.message(), StandardCharsets.UTF_8);
      String outputTopic = topicMapping.get(data.topic());

      if (outputTopic == null) {
        LOGGER.warn("No output topic mapping found for topic '{}'", data.topic());
        return;
      }

      kafkaPayload.setKey(key);
      kafkaPayload.setValue(line);
      kafkaPayload.setInputTopic(data.topic());
      kafkaPayload.setOutputTopic(outputTopic);

      try {
        final String[] fields = kafkaPayload.getValue().split("\t", -1);
        if (fields.length > 3) {
          synchronized (batchSync) {

            if(campaignFilter.allowedCampaign(fields[3], data.topic())) {

              logAndMonitorPayload.handleIncomingPayload(kafkaPayload);

              batch.add(kafkaPayload);
              currentTimeInMs = new DateTime(dateTimeZone).getMillis();
              batchTimedOut = (currentTimeInMs - batchStartTimeInMs >= batchTimeout);
              if (batchTimedOut) {
                LOGGER.info("Batch timed out for topic: {}", topic);
              }
              if (batch.size() >= batchSize || (!batch.isEmpty() && batchTimedOut)) {
                flushBatch();
              }
            }
          }
        } else {
          LOGGER.error("Invalid record in {}, payload = {}", threadName, kafkaPayload);
        }
      } catch (Exception ex) {
        LOGGER.error("Exception in processing event in consumer for thread: {}, payload: {}. Exception: {}",
                threadName, kafkaPayload, ex);
      }
    }

    LOGGER.debug("Shutting down thread {}", threadName);
  }

  private int findNextBatch(final int currentBatchIndex) {
    int batchIndex = currentBatchIndex;

    while (true) {
      LOGGER.debug("Finding available batch");
      batchIndex = (batchIndex + 1) % maxBatchLag;
      if (maxBatchLag != 1 && batchIndex == currentBatchIndex) {
        LOGGER.info("No batch empty for KafkaEventHandler, waiting for threads to finish..");
        try {
          Thread.sleep(StreamConstant.DEFAULT_THREAD_SLEEP_MS);
        } catch (InterruptedException e) {
          LOGGER.error("Thread interrupted in find next batch");
        }
      }

      final EventConsumerBatch consumerBatch = consumerBatches[batchIndex];
      if (Objects.isNull(consumerBatch.future) && consumerBatch.batch.isEmpty()) {
        LOGGER.debug("New EventConsumerBatch found at index {}", batchIndex);
        break;
      } else if (Objects.nonNull(consumerBatch.future) && consumerBatch.future.isDone()) {
        consumerBatch.clearEventConsumerBatch();
        LOGGER.debug("EventConsumerBatch processing at index {} done, clearing and reusing", batchIndex);
        break;
      }
    }

    return batchIndex;
  }

  private void flushBatch() {
    if (batch.size() > 0) {
      //Get the batch for the task
      batchIndex = findNextBatch(batchIndex);
      consumerBatches[batchIndex].batch.addAll(batch);

      //If there are too many jobs waiting in the queue, wait for sometime
      while (executorServiceWrapper.getQueueSize() > executorServiceWrapper.getThreadCount()) {
        LOGGER.debug("Consumer thread {} waiting for jb executor threads to get free", threadName);
        try {
          Thread.sleep(StreamConstant.DEFAULT_THREAD_SLEEP_MS);
        } catch (InterruptedException e) {
          LOGGER.error("Sleep interrupted for thread " + threadName);
        }
      }
      final EventHelper eventHelper = getEventHelper(consumerBatches[batchIndex].batch);
      consumerBatches[batchIndex].future = executorServiceWrapper.submitTask(eventHelper);

      // Check that batch submit task was successful
      // If not, free up the current batch from batchArray and wait for
      // some time
      if (Objects.isNull(consumerBatches[batchIndex].future)) {
        consumerBatches[batchIndex].clearEventConsumerBatch();
        LOGGER.error("Error in submitting task to jb executor service, retrying...");
        maxRetries--;
        try {
          Thread.sleep(StreamConstant.DEFAULT_THREAD_SLEEP_MS);
        } catch (InterruptedException e) {
          LOGGER.error("Sleep interrupted for thread " + threadName);
        }
        return;
      } else {
        maxRetries = maxExecuteRetries;
      }

      // If batch submit failed for max retries times, if yes exit the
      // process. We don't want to keep on reading events
      if (maxRetries <= 0) {
        LOGGER.error(
            "Error in submitting task, retry failed for {} times, please check errors. Exiting the process.", maxExecuteRetries);
        System.exit(1);
      }

      // If batch was successfully submitted, clean and move on to next
      batch.clear();
      // --next batch start time
      batchStartTimeInMs = new DateTime(dateTimeZone).getMillis();
    }
  }

  protected abstract EventHelper getEventHelper(final List<KafkaPayload> batch);

  private static class EventConsumerBatch {

    public List<KafkaPayload> batch;
    public Future<?> future;

    /**
     * @param batch EventConsumerBatch of events
     * @param future Future for the thread processing this batch
     */
    public EventConsumerBatch(List<KafkaPayload> batch, Future<?> future) {
      this.batch = batch;
      this.future = future;
    }

    public void clearEventConsumerBatch() {
      this.future = null;
      this.batch.clear();
    }
  }
}
