package co.lemnisk.transform.analyzepost.model.parser.v1;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class V1PageDataParser {
    private Page page;

    @Getter
    public static class Page {
        private String srcTimestamp = "";
        private Context context;
        private String messageId = UUID.randomUUID().toString();
        private Map<CharSequence, Object> otherIds;
        private Map<CharSequence, Object> pageProps;
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
                private String browser = "";
                private String ua = "";
                private String osType = "";
            }
        }

        @Getter
        public static class PageProps {
            private String path = "";
            private String referrer = "";
            private String name = "";
            private String title = "";
            private String url = "";
        }
    }
}
