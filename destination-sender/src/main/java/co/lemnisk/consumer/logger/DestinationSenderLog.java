package co.lemnisk.consumer.logger;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DestinationSenderLog extends EventLogger {
    private static final DestinationSenderLog destinationSenderLog = new DestinationSenderLog();

    private DestinationSenderLog() {
        this.logger = LoggerFactory.getLogger(DestinationSenderLog.class.getName());
    }

    public static DestinationSenderLog getInstance() {
        return destinationSenderLog;
    }
}

