package co.lemnisk.loggers;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DestTransformerInputEventLogger extends EventLogger {
    private static final DestTransformerInputEventLogger destinationEventLogger = new DestTransformerInputEventLogger();

    private DestTransformerInputEventLogger() {
        this.logger = LoggerFactory.getLogger(DestTransformerInputEventLogger.class.getName());
    }

    public static DestTransformerInputEventLogger getInstance() {
        return destinationEventLogger;
    }
}
