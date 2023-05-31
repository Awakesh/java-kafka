package co.lemnisk.loggers;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class DmpSstEventLogger extends EventLogger {
    private static final DmpSstEventLogger dmpSstEventLogger = new DmpSstEventLogger();

    private DmpSstEventLogger() {
        this.logger = LoggerFactory.getLogger(DmpSstEventLogger.class.getName());
    }

    public static DmpSstEventLogger getInstance() {
        return dmpSstEventLogger;
    }
}
