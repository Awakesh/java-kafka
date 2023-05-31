package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.metrics.MonitoringHelper;
import co.lemnisk.transform.CampaignIdParser;
import lombok.Getter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public abstract class DmpSstDataBuilder<T> {

    protected HashMap<String, String> data;
    @Getter
    protected String rawData;
    protected HashMap<CharSequence, Object> properties;

    public abstract void constructData();
    public abstract String eventType();
    public abstract T getData();

    public String getCampaignId() {
        return get("id");
    }

    public String getEventName() {
        return MonitoringHelper.getEventName(eventType(), userEventName());
    }

    public HashMap<CharSequence, Object> getProperties() {
        return properties;
    }

    public String getSrcId() {
        return get("srcid");
    }

    public String getId(){
        return get("cb");
    }

    public CharSequence getAccountId() {
        return CampaignIdParser.parse(get("account_id"));
    }

    protected String userEventName() {
        return "";
    }

    protected DmpSstDataBuilder(HashMap<String, String> payload, String rawData, String propertiesKeyPrefix) {
        this.rawData = rawData;
        this.data = payload;
        setProperties(propertiesKeyPrefix);
    }

    protected DmpSstDataBuilder(HashMap<String, String> payload, String rawData) {
        this(payload, rawData, null);
    }

    protected String get(String key) {
        key = decodeString(key);

        return decodeString(data.getOrDefault(key, ""));
    }

    protected HashMap<CharSequence, Object> getOtherIds() {
        HashMap<CharSequence, Object> map = new HashMap<>();

        map.put("_fbp", get("fbp"));
        map.put("_ga", get("ga"));
        map.put("email", get("fp55"));
        map.put("phone", get("fp56"));

        return map;
    }

    private void setProperties(String keyPrefix) {
        this.properties = new HashMap<>();
        if (keyPrefix == null){
            return ;
        }
        for (var entry : data.entrySet()) {
            if (entry.getKey().startsWith(keyPrefix)) {
                String key = entry.getKey().replaceAll('^' + keyPrefix, "");
                this.properties.put(key, entry.getValue());
            }
        }
    }

    protected String decodeString(String str) {
        if (str == null) return "";

        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

}
