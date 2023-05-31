package co.lemnisk.common.logging;

import org.slf4j.LoggerFactory;

public class DestTransformerOutputEventLogger extends EventLogger {
    private static final DestTransformerOutputEventLogger eventLogger = new DestTransformerOutputEventLogger();

    private DestTransformerOutputEventLogger() {
        this.logger = LoggerFactory.getLogger(DestTransformerOutputEventLogger.class.getName());
    }

    public static DestTransformerOutputEventLogger getInstance() {
        return eventLogger;
    }
}
