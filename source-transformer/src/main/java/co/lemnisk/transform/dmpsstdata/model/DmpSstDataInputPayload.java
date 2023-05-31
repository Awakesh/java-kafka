package co.lemnisk.transform.dmpsstdata.model;

import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.dmpsstdata.builder.*;
import co.lemnisk.utils.IPChecker;
import co.lemnisk.validation.Validation;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class DmpSstDataInputPayload {

    private HashMap<String, String> data;
    private String rawData;

    public void process(String rawData, String payloadData) {

        this.rawData = rawData;
        String[] splitData = payloadData.split("\t");
        setData(splitData);
    }

    private void setData(String[] splitData) {
        String[] props = splitData[2].split("&");
        this.data = new HashMap<>();

        for (String val : props) {
            String[] propArr = val.split("=");

            if (propArr.length == 2) {
                data.put(propArr[0], URLDecoder.decode(propArr[1], StandardCharsets.UTF_8));
            }
        }

        setIp(splitData);
        setServerTs(splitData);
        checkAndUpdateMessageId();
    }

    private void setIp(String[] splitData) {

        // Starting from 3rd index as first 3 elements won't be IP address
        for (int i = 3; i < splitData.length; i++) {
            if (IPChecker.isValidIPAddress(splitData[i])) {
                this.data.put("ip", splitData[i]);
                break;
            }
        }
    }

    private void setServerTs(String[] splitData) {
        this.data.put(SourceTransformerConstant.SERVER_TS_KEY, splitData[22]);
    }

    public void validateWithException() {
        String ipAddress = data.get("ip");
        String accountId = String.valueOf(CampaignIdParser.parse(data.get("account_id")));
        String messageId = data.get("messageId");
        String serverTs = data.get("server_ts");
        String srcId = data.get("srcid");

        Validation validationObj = new Validation();

        validationObj.validate(messageId, ipAddress, accountId,serverTs, srcId);
    }

    public DmpSstDataBuilder<?> getDataBuilder() {

        if (isPageEvent())
            return new DmpSstDataPageTypeBuilder(data, rawData);
        else if (isIdentifyEvent())
            return new DmpSstDataIdentifyTypeBuilder(data, rawData);
        else if (isTrackEvent())
            return new DmpSstDataTrackTypeBuilder(data, rawData);
        else
            return new DmpSstDataUnknownTypeBuilder(data, rawData);
    }

    private void checkAndUpdateMessageId() {
        String messageId = data.get("messageId");

        if(StringUtils.isBlank(messageId))
            data.put("messageId", UUID.randomUUID().toString());
    }

    private boolean isPageEvent() {
        return eventType().equals(EventType.PAGE);
    }

    private boolean isTrackEvent() {
        return eventType().equals(EventType.TRACK);
    }

    private boolean isIdentifyEvent() {
        return eventType().equals(EventType.IDENTIFY);
    }

    private String eventType() {
        return data.getOrDefault("event_type", EventType.UNKNOWN);
    }
}
