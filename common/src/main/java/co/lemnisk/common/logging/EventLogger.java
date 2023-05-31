package co.lemnisk.common.logging;

import org.slf4j.Logger;

public abstract class EventLogger {
    public Logger logger = null;

    public enum MESSAGE_FLOW {
        INCOMING,
        OUTGOING
    }

    public void log(String topic, EventLogger.MESSAGE_FLOW messageFlow, String payload) {
        logger.info("{} - {} - {}", topic, messageFlow, payload);
    }
}
