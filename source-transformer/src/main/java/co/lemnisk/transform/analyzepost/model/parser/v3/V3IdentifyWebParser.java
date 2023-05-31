package co.lemnisk.transform.analyzepost.model.parser.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter
public class V3IdentifyWebParser {

    private String id = "";
    private String userId = "";
    private String messageId = "";
    private String receivedAt = "";
    private String sentAt = "";
    private String timestamp = "";
    private String type = "";
    private String originalTimestamp = "";
    private String writeKey = "";

    private HashMap<CharSequence, Object> otherIds = new LinkedHashMap<>();
    private HashMap<CharSequence, Object> customerProperties = new LinkedHashMap<>();
    private Context context;

    @Getter
    public static class Context {
        private Library library;
        private UserAgent userAgent;
        private String ip = "";
        private Page page;
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
    public static class Page {
        private String path = "";
        private String referrer = "";
        private String search = "";
        private String title = "";
        private String url = "";
    }

    @Getter
    public static class UserAgent {
        private String deviceType = "";
        private String osType = "";
        private String osVersion = "";
        private String browser = "";
        private String ua = "";
    }
}
