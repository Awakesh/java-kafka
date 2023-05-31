package co.lemnisk.transform.dmpsstdata.builder;

import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.avro.model.event.track.web.*;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DmpSstDataTrackTypeBuilder extends DmpSstDataBuilder<TrackWeb> {

    private TrackWeb trackWeb;
    private static final String PROPERTIES_KEY_PREFIX = "t_";
    private Boolean isStandardEvent;
    private CDPCustomEventsDictionary cdpCustomEventsDictionary;

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    @Autowired
    EventDictionaryService eventDictionaryService;

    public DmpSstDataTrackTypeBuilder(HashMap<String, String> payload, String rawData) {
        super(payload, rawData, PROPERTIES_KEY_PREFIX);
    }

    @Override
    public String eventType() {
        return EventType.TRACK;
    }

    @Override
    public TrackWeb getData() {
        return trackWeb;
    }

    @Override
    protected String userEventName() {
        return decodeString((String)properties.get("event_name"));
    }

    @Override
    public void constructData() {
        constructEventMappingData();

        this.trackWeb = TrackWeb.newBuilder()
                .setProperties(this.properties)
                .setOtherIds(getOtherIds())
                .setContext(getContext())
                .setServerTs(get("server_ts"))
                .setUserId(get("userid"))
                .setAmpId(get("ampid"))
                .setMessageId(get("messageId"))
                .setSentAt(get("sentTime"))
                .setOriginalTimestamp(get("originalTimestamp"))
                .setId(get("cb"))
                .setType(eventType())
                .setWriteKey(getWriteKey())
                .setEvent(userEventName())
                .setLemEvent(getLemEvent())
                .setIsStandardEvent(isStandardEvent)
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
                .setPage(getPageDetails())
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
                .setBrowser(get("browser")) // TODO: browser key is not yet tested
                .setOsVersion(get("os_version"))
                .setUa(get("userAgent"))
                .build();
    }

    // TODO: UTM is not yet tested
    private ContextUtm getUtm() {

        return ContextUtm.newBuilder()
                .setCampaign(get("utm_campaign"))
                .setSource(get("utm_source"))
                .setMedium(get("utm_medium"))
                .setTerm(get("utm_term"))
                .setContent(get("utm_content"))
                .build();
    }

    private ContextPage getPageDetails() {
        return ContextPage.newBuilder()
                .setTitle(get("page_title"))
                .setPath(get("page_path"))
                .setReferrer(get("page_referrer"))
                .setUrl(get("page_url"))
                .setSearch(get("page_search"))
                .build();
    }

    private void constructEventMappingData() {
        Integer accountId = Integer.parseInt(String.valueOf(getAccountId()));
        String eventName = userEventName();
        Map.Entry<Boolean, CDPCustomEventsDictionary> eventsDictionaryEntry = eventDictionaryService.getEventDictionaryData(accountId, eventName, "track");
        this.isStandardEvent = eventsDictionaryEntry.getKey();
        this.cdpCustomEventsDictionary = eventsDictionaryEntry.getValue();
    }

    private String getLemEvent() {
        return ((cdpCustomEventsDictionary != null && cdpCustomEventsDictionary.getLemEventName() != null) ? cdpCustomEventsDictionary.getLemEventName() : "");
    }

}
