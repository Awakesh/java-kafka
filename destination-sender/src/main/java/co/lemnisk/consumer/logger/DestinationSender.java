package co.lemnisk.consumer.logger;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DestinationSender extends EventLogger {
    private static final DestinationSender destinationSender = new DestinationSender();

    private DestinationSender() {
        this.logger = LoggerFactory.getLogger(DestinationSender.class.getName());
    }

    public static DestinationSender getInstance() {
        return destinationSender;
    }
}
