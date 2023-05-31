package co.lemnisk.data.migration.config;

import co.lemnisk.data.migration.constants.DataMigrationConstant;
import co.lemnisk.utils.streamClient.consumers.KafkaConsumer;
import co.lemnisk.utils.streamClient.consumers.StreamConsumer;
import co.lemnisk.utils.streamClient.executor.ExecutorServiceWrapper;
import co.lemnisk.utils.streamClient.handlers.StreamConsumerHandlerFactory;
import kafka.consumer.KafkaStream;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerConfig.class);

  public PropertiesConfiguration propertyConfig() {
    try {
      PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
      propertiesConfiguration.setDelimiterParsingDisabled(true);
      propertiesConfiguration.load(DataMigrationConstant.PROD_KAFKA_CONFIG_PATH);
      return propertiesConfiguration;
    } catch (ConfigurationException e) {
      LOGGER.error("Exception in loading configuration", e);
    }
    return null;
  }

  @Bean(destroyMethod = "shutdown")
  public StreamConsumer streamConsumer(
          @Qualifier("kafkaConsumerHandlerFactory") final StreamConsumerHandlerFactory<KafkaStream<byte[], byte[]>> streamConsumerHandlerFactory) {
    return new KafkaConsumer(propertyConfig(), streamConsumerHandlerFactory);
  }

  @Bean(destroyMethod = "shutdown")
  public ExecutorServiceWrapper executorServiceWrapper() {
    return new ExecutorServiceWrapper(propertyConfig());
  }

}
