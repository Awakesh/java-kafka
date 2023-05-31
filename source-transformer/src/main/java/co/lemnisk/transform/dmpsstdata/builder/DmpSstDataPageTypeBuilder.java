package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.avro.model.event.page.*;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class DmpSstDataPageTypeBuilder extends DmpSstDataBuilder<Page> {

    private Page page;
    private static final String PROPERTIES_KEY_PREFIX = "p_";

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    public DmpSstDataPageTypeBuilder(HashMap<String, String> payload, String rawData) {
        super(payload, rawData, PROPERTIES_KEY_PREFIX);
    }

    @Override
    public String eventType() {
        return EventType.PAGE;
    }

    public Page getData() {
        return this.page;
    }

    @Override
    public void constructData() {
        this.page = Page.newBuilder()
                .setAmpId(get("ampid"))
                .setProperties(this.properties)
                .setOtherIds(getOtherIds())
                .setContext(getContext())
                .setServerTs(get("server_ts"))
                .setMessageId(get("messageId"))
                .setSentAt(get("sentTime"))
                .setOriginalTimestamp(get("originalTimestamp"))
                .setId(get("cb"))
                .setType(eventType())
                .setWriteKey(getWriteKey())
                .setIsStandardEvent(true)
                .build();
    }

    private String getWriteKey() {
        String srcId = getSrcId();
        String writeKey = get("writeKey");
        if (StringUtils.isNotEmpty(srcId) && StringUtils.isEmpty(writeKey)) {
            CDPSourceInstance sourceInstance = cdpSourceInstanceService.getCDPSourceInstance(Integer.parseInt(srcId));
            writeKey = (sourceInstance != null ? sourceInstance.getWriteKey() : "");
        }
        return writeKey;
    }

    private Context getContext() {

        return Context.newBuilder()
                .setLibrary(getLibrary())
                .setUserAgent(getUserAgent())
                .setUtm(getUtm())
                .setIp(get("ip"))
                .setSrcId(getSrcId())
                .setAccountId(getAccountId())
                .build();
    }

    private ContextLibrary getLibrary() {

        return ContextLibrary.newBuilder()
                .setName(get("library_name"))
                .setVersion(get("library_version"))
                .build();
    }

    private ContextUserAgent getUserAgent() {

        return ContextUserAgent.newBuilder()
                .setDeviceType(get("device_type"))
                .setOsType(get("os_name"))
                .setBrowser(get("browser"))
                .setOsVersion(get("os_version"))
                .setUa(get("userAgent"))
                .build();
    }

    private ContextUtm getUtm() {

        return ContextUtm.newBuilder()
                .setCampaign(get("utm_campaign"))
                .setSource(get("utm_source"))
                .setMedium(get("utm_medium"))
                .setTerm(get("utm_term"))
                .setContent(get("utm_content"))
                .build();
    }

}
