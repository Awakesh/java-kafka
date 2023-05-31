package co.lemnisk.loggers;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class RouterEventLogger extends EventLogger {
    private static final RouterEventLogger routerEventLogger = new RouterEventLogger();

    private RouterEventLogger() {
        this.logger = LoggerFactory.getLogger(RouterEventLogger.class.getName());
    }

    public static RouterEventLogger getInstance() {
        return routerEventLogger;
    }
}