package co.lemnisk.transform.analyzepost.model.parser.v3;

import co.lemnisk.common.metrics.MonitoringHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public class V3PageParser {

    private String id = "";
    private String userId = "";
    private String name = "";
    private String messageId = "";
    private String receivedAt = "";
    private String sentAt = "";
    private String timestamp = "";
    private String type = "";
    private String originalTimestamp = "";
    private String writeKey = "";

    private HashMap<CharSequence, Object> otherIds = new LinkedHashMap<>();
    private HashMap<CharSequence, Object> properties = new LinkedHashMap<>();
    private Context context;

    @Getter
    public static class Context {
        private Library library;
        private UserAgent userAgent;
        private Utm utm;
        private String ip = "";
        @JsonProperty("account_id")
        private String accountId = "";
        @JsonProperty("srcid")
        private int srcId;
        @JsonProperty("server_ts")
        private String server_ts = "";
    }

    @Getter
    public static class Library {
        private String name = "";
        private String version = "";
    }

    @Getter
    public static class UserAgent {
        private String deviceType = "";
        private String osType = "";
        private String osVersion = "";
        private String browser = "";
        private String ua = "";
    }

    @Getter
    public static class Utm {
        private String campaign = "";
        private String source = "";
        private String medium = "";
        private String term = "";
        private String content = "";
    }

    public String getEventName() {
        return MonitoringHelper.getEventName(getType(), userEventName());
    }

    private String userEventName() {
        return "";
    }
}
