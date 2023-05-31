package co.lemnisk.transform.analyzepost.builder.v3;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.avro.model.event.track.web.*;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v3.V3TrackWebParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@NoArgsConstructor
public class V3TrackWebBuilder {

    private V3TrackWebParser data;
    private V3TrackWebParser.Context context;
    private V3TrackWebParser.Library library;
    private V3TrackWebParser.UserAgent userAgent;
    private V3TrackWebParser.Page page;
    private V3TrackWebParser.Utm utm;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;
    private Boolean isStandardEvent;
    private CDPCustomEventsDictionary cdpCustomEventsDictionary;

    @Autowired
    EventDictionaryService eventDictionaryService;

    public V3TrackWebBuilder(String data) throws JsonProcessingException {
        setRawData(data);
    }

    public V3TrackWebBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(rawData, V3TrackWebParser.class);
        return this;
    }

    public V3TrackWebBuilder validateWithException(){
        String ipAddress = data.getContext().getIp();
        String accountId = String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId()));
        String messageId = data.getMessageId();
        String serverTs = data.getContext().getServer_ts();
        String srcId = String.valueOf(data.getContext().getSrcId());
        Validation validationObj = new Validation();

        validationObj.validate(messageId, ipAddress, accountId, serverTs, srcId);

        return this;
    }

    public void addMetrics(EventMonitoring eventMonitoring){
        eventMonitoring.addCdpMetric(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())),
                String.valueOf(data.getContext().getSrcId()),
                data.getEvent(),
                data.getType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())),
                String.valueOf(data.getContext().getSrcId()),
                data.getEvent(),
                data.getProperties(),
                data.getType());

    }

    public TrackWeb build() {
        this.context= this.data.getContext();
        this.library = this.data.getContext().getLibrary();
        this.userAgent = this.data.getContext().getUserAgent();
        this.utm = this.data.getContext().getUtm();
        this.page = this.data.getContext().getPage();
        this.otherIds = this.data.getOtherIds();
        this.properties = this.data.getProperties();
        constructEventMappingData();

        return getTrackData()
                .build();
    }

    private ContextUserAgent.Builder getContextUserAgent(){
        return ContextUserAgent.newBuilder()
                .setOsType(userAgent.getOsType())
                .setOsVersion(userAgent.getOsVersion());
    }

    private ContextUtm.Builder getContextUtm(){
        ContextUtm.Builder utmBuilder = ContextUtm.newBuilder();

        if (utm != null) {
            utmBuilder
                    .setCampaign(utm.getCampaign())
                    .setContent(utm.getContent())
                    .setMedium(utm.getMedium())
                    .setSource(utm.getSource())
                    .setTerm(utm.getTerm());
        }

        return utmBuilder;
    }

    private ContextPage.Builder getContextPage(){
        return ContextPage.newBuilder()
                .setPath(page.getPath())
                .setReferrer(page.getReferrer())
                .setSearch(page.getSearch())
                .setTitle(page.getTitle())
                .setUrl(page.getUrl());
    }

    private ContextLibrary.Builder getContextLibrary(){
        return ContextLibrary.newBuilder()
                .setName(library.getName())
                .setVersion(library.getVersion());

    }

    private Context.Builder getContextData(){
        return Context.newBuilder()
                .setUserAgentBuilder(getContextUserAgent())
                .setPageBuilder(getContextPage())
                .setIp(this.data.getContext().getIp())
                .setUtmBuilder(getContextUtm())
                .setLibraryBuilder(getContextLibrary())
                .setAccountId(CampaignIdParser.parse(data.getContext().getAccountId()))
                .setSrcId(String.valueOf(data.getContext().getSrcId()));
    }

    private void constructEventMappingData() {
        int accountId = Integer.parseInt(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())));
        Map.Entry<Boolean, CDPCustomEventsDictionary> eventsDictionaryEntry = eventDictionaryService.getEventDictionaryData(accountId, data.getEvent(), "track");
        this.isStandardEvent = eventsDictionaryEntry.getKey();
        this.cdpCustomEventsDictionary = eventsDictionaryEntry.getValue();
    }

    private CharSequence getLemEventName() {
        return ((cdpCustomEventsDictionary != null && cdpCustomEventsDictionary.getLemEventName() != null) ? cdpCustomEventsDictionary.getLemEventName() : "");
    }

    private TrackWeb.Builder getTrackData(){
        return TrackWeb.newBuilder()
                .setContextBuilder(getContextData())
                .setId(data.getId())
                .setLemEvent(getLemEventName())
                .setMessageId(data.getMessageId())
                .setOtherIds(otherIds)
                .setServerTs(data.getContext().getServer_ts())
                .setOriginalTimestamp(data.getOriginalTimestamp())
                .setProperties(properties)
                .setReceivedAt(data.getReceivedAt())
                .setTimestamp(data.getTimestamp())
                .setWriteKey(data.getWriteKey())
                .setType(EventType.TRACK)
                .setEvent(data.getEvent())
                .setSentAt(data.getSentAt())
                .setUserId(data.getUserId())
                .setIsStandardEvent(isStandardEvent);
    }

}
