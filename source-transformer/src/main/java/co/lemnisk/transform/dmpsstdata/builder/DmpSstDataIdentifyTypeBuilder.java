package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.avro.model.event.identify.web.*;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class DmpSstDataIdentifyTypeBuilder extends DmpSstDataBuilder<IdentifyWeb> {

    private IdentifyWeb identifyWeb;
    private static final String PROPERTIES_KEY_PREFIX = "i_";

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    public DmpSstDataIdentifyTypeBuilder(HashMap<String, String> payload, String rawData) {
        super(payload, rawData, PROPERTIES_KEY_PREFIX);
    }

    @Override
    public String eventType() {
        return EventType.IDENTIFY;
    }

    @Override
    public IdentifyWeb getData() {
        return identifyWeb;
    }

    @Override
    public void constructData() {
        this.identifyWeb = IdentifyWeb.newBuilder()
                .setCustomerProperties(this.properties)
                .setOtherIds(getOtherIds())
                .setContext(getContext())
                .setServerTs(get("server_ts"))
                .setMessageId(get("messageId"))
                .setSentAt(get("sentTime"))
                .setOriginalTimestamp(get("originalTimestamp"))
                .setId(get("cb"))
                .setType(eventType())
                .setPage(getPageDetails())
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

    private Page getPageDetails() {
        return Page.newBuilder()
                .setTitle(get("page_title"))
                .setPath(get("page_path"))
                .setReferrer(get("page_referrer"))
                .setUrl(get("page_url"))
                .setSearch(get("page_search"))
                .build();
    }

}
