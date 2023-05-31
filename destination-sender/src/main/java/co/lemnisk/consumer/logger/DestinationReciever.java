package co.lemnisk.consumer.logger;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DestinationReciever extends EventLogger {
    private static final DestinationReciever destinationReciever = new DestinationReciever();

    private DestinationReciever() {
        this.logger = LoggerFactory.getLogger(DestinationReciever.class.getName());
    }

    public static DestinationReciever getInstance() {
        return destinationReciever;
    }
}

