package co.lemnisk.transform.analyzepost.model.parser.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public class V3ScreenParser {

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
        private App app;
        private Screen screen;
        private Device device;
        private UserAgent userAgent;
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
    public static class App {
        private String name = "";
        private String version = "";
        private String build = "";
    }

    @Getter
    public static class Screen {
        private String width = "";
        private String height = "";
        private String density = "";
    }

    @Getter
    public static class Device {
        private String id = "";
        private String advertisingId = "";
        private boolean adTrackingEnabled = true;
        private String manufacturer = "";
        private String model = "";
        private String type = "";
        private String token = "";
    }

    @Getter
    public static class UserAgent {
        private String osType = "";
        private String osVersion = "";
    }
}
