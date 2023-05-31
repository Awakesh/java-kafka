package co.lemnisk.consumer.listener;


import co.lemnisk.consumer.controller.ControllerExceptionHandler;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

import co.lemnisk.consumer.configuration.CustomKafkaListenerProperties;
import co.lemnisk.consumer.model.CustomKafkaListenerProperty;

@Component
public class CustomKafkaListenerRegistrar implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomKafkaListenerRegistrar.class);

    @Autowired
    private CustomKafkaListenerProperties customKafkaListenerProperties;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @Override
    public void afterPropertiesSet() {
        customKafkaListenerProperties.getListeners()
                .forEach(this::registerCustomKafkaListener);
    }

    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty) {
        this.registerCustomKafkaListener(name, customKafkaListenerProperty, false);
    }

    @SneakyThrows
    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty, boolean startImmediately) {
        String listenerClass = String.join(".", CustomKafkaListenerRegistrar.class.getPackageName(), customKafkaListenerProperty.getListenerClass());
        LOGGER.info("Listener class {}", listenerClass);
        CustomMessageListener customMessageListener = (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        kafkaListenerEndpointRegistry.registerListenerContainer(customMessageListener.createKafkaListenerEndpoint(name, customKafkaListenerProperty.getTopic()), kafkaListenerContainerFactory, startImmediately);
    }
}
