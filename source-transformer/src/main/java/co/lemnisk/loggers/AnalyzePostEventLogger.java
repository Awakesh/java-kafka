package co.lemnisk.loggers;

import co.lemnisk.common.logging.EventLogger;
import org.slf4j.LoggerFactory;

public class AnalyzePostEventLogger extends EventLogger {
    private static final AnalyzePostEventLogger analyzePostEventLogger = new AnalyzePostEventLogger();

    private AnalyzePostEventLogger() {
        this.logger = LoggerFactory.getLogger(AnalyzePostEventLogger.class.getName());
    }

    public static AnalyzePostEventLogger getInstance() {
        return analyzePostEventLogger;
    }
}
