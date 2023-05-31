package co.lemnisk.common.constants;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SourceTransformerConstant {
    public static final String PAYLOAD_SEPARATOR="\t";
    public static final Duration ALLOWED_INCOMING_DURATION_MIN = Duration.ofMinutes(24 * 60);

    // DeDuplication Duration
    // TODO: Get this from ConfigMap
    public static final long PAYLOAD_DE_DUPE_INTERVAL = TimeUnit.MINUTES.toMillis(240);

    // Length of time to retain data in the store
    public static final Integer DE_DUPE_RETENTION_PERIOD_MIN=300; // 5 hours

    // Size of the windows (cannot be negative)
    public static final Integer DE_DUPE_WINDOW_SIZE_MIN=15;

    public static final String SERVER_TS_KEY="server_ts";

    public static final String ANDROID_REGEX=".*\"name\":\\s*\"lemnisk-android\".*";
    public static final String IOS_REGEX=".*\"type\":\\s*\"iOS\\s*SDK\".*";

    // Only allow Livspace, Livspace Expert and DSP Campaigns
    // "6112", "6142", "6153", "6154" - Not used for now
    // 4658 - Only for testing
    public static final List<String> ALLOWED_ACCOUNT_IDS = Arrays.asList("6081", "6221", "6233", "4658");

}
