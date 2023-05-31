package co.lemnisk.common.constants;

public class Constants {

    public static final String CONFLUENT_CONFIG_PATH="/disk1/config/kafka/confluent.properties";
    public static final String CLOUD_MODE="{\"mode\":\"cloud\"}";

    public static final String CONTEXT_ACCOUNT_ID = "$.context.accountId";
    public static final String DESTINATION_INSTANCE_ID = "$.destinationInstanceId";
    public static final String TYPE = "$.type";
    public static final String EVENT = "$.event";
    public static final String PROPERTIES = "$.properties";
    public static final String SRC_ID = "$.context.srcId";

    public static final String URI_CREATE ="/api/kafka/create";
    public static final String GET_CONSUMER_IDS = "/api/kafka/getConsumerIds";
    public static final String DLT = "DLT";

    public static class ModuleNames {
        public static final String SOURCE_TRANSFORMER = "SourceTransformer";
        public static final String ROUTER = "Router";
        public static final String DESTINATION_TRANSFORMER = "DestinationTransformer";
        public static final String DESTINATION_SENDER = "DestinationSender";
    }

    public static class EventTypes {
        public static final String PAGE = "page";
        public static final String TRACK = "track";
        public static final String IDENTIFY = "identify";
        public static final String SCREEN = "screen";
    }

}