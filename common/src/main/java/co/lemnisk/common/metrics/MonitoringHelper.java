package co.lemnisk.common.metrics;


import co.lemnisk.common.constants.Constants;

public class MonitoringHelper {

    public static String getEventName(String eventType, String userEvent) {

        switch (eventType) {
            case Constants.EventTypes.IDENTIFY:
            case Constants.EventTypes.SCREEN:
                return eventType;
            case Constants.EventTypes.PAGE:
                return "pageview";
            case Constants.EventTypes.TRACK:
                return  userEvent;
            default:
                return "unknown";
        }
    }
}
