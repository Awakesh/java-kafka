package co.lemnisk.loggers;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DmpNbaEventLogger extends EventLogger {
    private static final DmpNbaEventLogger dmpNbaEventLogger = new DmpNbaEventLogger();

    private DmpNbaEventLogger() {
        this.logger = LoggerFactory.getLogger(DmpNbaEventLogger.class.getName());
    }

    public static DmpNbaEventLogger getInstance() {
        return dmpNbaEventLogger;
    }
}
