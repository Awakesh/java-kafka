package co.lemnisk.transform.analyzepost.model.parser.v1;

import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.MonitoringHelper;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class V1IdentifyWebDataParser {
    private Identify identify;

    public String getEventName() {
        return MonitoringHelper.getEventName(EventType.IDENTIFY, userEventName());
    }

    private String userEventName() {
        return "";
    }

    @Getter
    public static class Identify {
        private Map<CharSequence, Object> properties = new LinkedHashMap<>();

        @JsonAnySetter
        void setProperties(String key, String value) {
            properties.put(key, value);
        }

        private IdentifyProps identifyProps;

        @Getter
        public static class IdentifyProps {
            private String srcTimestamp = "";
            private Context context;
            private String messageId = UUID.randomUUID().toString();
            private Map<CharSequence, Object> otherIds = new LinkedHashMap<>();
            private String id = "";
            private String lemTimestamp = "";

            @Getter
            public static class Context {
                private String writeKey = "";
                private Library library;
                private int account_id;
                private int srcid;
                private String ip = "";
                private UserAgent userAgent;

                @Getter
                public static class Library {
                    private String name = "";
                    private String version = "";
                }

                @Getter
                public static class UserAgent {
                    private String deviceType = "";
                    private String osVersion = "";
                    private String osType = "";
                    private String ua = "";
                    @JsonProperty("Browser")
                    private String browser = "";
                }
            }
        }
    }
}
