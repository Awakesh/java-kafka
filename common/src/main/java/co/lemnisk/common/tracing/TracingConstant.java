package co.lemnisk.common.tracing;

public class TracingConstant {
    public static final String SPAN_ID="spanId";
    public static final String TRACER_ID="tracerId";
    public static final String TRACER_NAME="Transformer";

    public interface Tags {
        String PAYLOAD_TYPE = "payloadType"; // For V1, V2, & V3 of analyze_type topic
        String MESSAGE_ID = "messageId";
        String CAMPAIGN_ID = "campaignId";
        String EVENT_TYPE = "eventType";
        String DESTINATION_INSTANCE_ID = "DestinationInstanceId";
    }

    public interface SpanNames {
        String EVENT_DEDUPLICATION = "EventDeduplication";
        String L1_TRANSFORMER_V1 = "L1TransformerV1";
        String L1_TRANSFORMER_V2 = "L1TransformerV2";
        String L1_TRANSFORMER_V3 = "L1TransformerV3";
        String L1_TRANSFORMER_UNKNOWN = "L1TransformerUnknown";
        String L2_V1_TRACK_WEB = "L2V1TrackWebTransformer";
        String L2_V1_PAGE = "L2V1PageTransformer";
        String L2_V1_IDENTIFY_WEB = "L2V1IdentifyWebTransformer";
        String L2_V2_TRACK_APP = "L2V2TrackAppTransformer";
        String L2_V2_SCREEN = "L2V2ScreenTransformer";
        String L2_V2_IDENTIFY_APP = "L2V2IdentifyAppTransformer";
        String L2_V3_TRACK_WEB = "L2V3TrackWebTransformer";
        String L2_V3_PAGE = "L2V3PageTransformer";
        String L2_V3_IDENTIFY_WEB = "L2V3IdentifyWebTransformer";
        String L2_V3_TRACK_APP = "L2V3TrackAppTransformer";
        String L2_V3_SCREEN = "L2V3ScreenTransformer";
        String L2_V3_IDENTIFY_APP = "L2V3IdentifyAppTransformer";
        String DMP_NBA_INITIALIZE_BUILDER = "DmpNbaInitializeBuilder";
        String DMP_NBA_EVENT_TRANSFORMER = "DmpNbaEventTransformer";
        String DMP_SST_INITIALIZE_BUILDER = "DmpSstInitializeBuilder";
        String DMP_SST_TRACK_WEB = "DmpSstTrackWebTransformer";
        String DMP_SST_IDENTIFY_WEB = "DmpSstIdentifyWebTransformer";
        String DMP_SST_PAGE = "DmpSstPageTransformer";
        String DMP_SST_UNKNOWN = "DmpSstUnknownTransformer";
        String DESTINATION_S3 = "DestinationSenderS3";
        String DESTINATION_SENDER = "DestinationSender";
    }
}
