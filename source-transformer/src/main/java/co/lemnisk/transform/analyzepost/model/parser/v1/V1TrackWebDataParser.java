package co.lemnisk.transform.analyzepost.model.parser.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class V1TrackWebDataParser {
    private String evtnm;
    private Track track;

    @Getter
    public class Track {
        private String eventName;
        private TrackProps trackProps;
        private Map<CharSequence, Object> eventProperties;

        @Getter
        public class TrackProps {
            private String srcTimestamp;
            private Context context;
            private String messageId = UUID.randomUUID().toString();
            private Map<CharSequence, Object> otherIds;
            private String id;
            private String lemTimestamp;

            @Getter
            public class Context {
                private String writeKey;
                private Library library;
                private int account_id;
                private int srcid;
                private String ip;
                private UserAgent userAgent;
                private Page page;

                @Getter
                public class Library {
                    private String name;
                    private String version;
                }

                @Getter
                public class Page {
                    private String path;
                    private String referrer;
                    private String title;
                    private String url;
                }

                @Getter
                public class UserAgent {
                    private String deviceType;
                    private String osVersion;
                    private String osType;
                    private String ua;
                    @JsonProperty("Browser")
                    private String browser;
                }
            }
        }
    }
}
