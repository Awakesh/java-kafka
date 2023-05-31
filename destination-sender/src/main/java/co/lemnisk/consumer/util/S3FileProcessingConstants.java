package co.lemnisk.consumer.util;

public class S3FileProcessingConstants {

    public static final String DESTINATION_SENDER_LOG_ROOT = "/disk1/logs/destination-sender";
    public static final String FILE_PROCESSING = "PROCESSING";
    public static final String FILE_PROCESSED = "PROCESSED";
    public static final String UPLOADED_CLIENT = "UPLOADED/CLIENT";
    public static final String UPLOADED_LEMNISK = "UPLOADED/LEMNISK";
    public static final String FAILED_UPLOAD_CLIENT = "FAILED/CLIENT";
    public static final String FAILED_UPLOAD_LEMNISK = "FAILED/LEMNISK";
    public static final int MAX_RETRIES_S3_UPLOAD = 3;

}
