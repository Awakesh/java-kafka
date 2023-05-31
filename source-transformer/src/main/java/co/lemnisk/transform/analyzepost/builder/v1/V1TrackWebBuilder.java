package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.avro.model.event.track.web.*;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v1.V1TrackWebDataParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@NoArgsConstructor
public class V1TrackWebBuilder extends AbstractV1DataBuilder {

    @Autowired
    EventDictionaryService eventDictionaryService;

    private V1TrackWebDataParser data = null;
    private CDPCustomEventsDictionary cdpCustomEventsDictionary = null;
    private V1TrackWebDataParser.Track trackData;
    private V1TrackWebDataParser.Track.TrackProps trackProps;
    private V1TrackWebDataParser.Track.TrackProps.Context context;
    private V1TrackWebDataParser.Track.TrackProps.Context.Library library;
    private V1TrackWebDataParser.Track.TrackProps.Context.UserAgent userAgent;
    private V1TrackWebDataParser.Track.TrackProps.Context.Page page;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;
    private boolean isStandardEvent;

    public V1TrackWebBuilder(String inputJsonStr) throws JsonProcessingException {
        setData(inputJsonStr);
    }

    public void setData(String inputJsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(inputJsonStr, V1TrackWebDataParser.class);
    }

    public V1TrackWebBuilder validateWithException() {
        String ipAddress = data.getTrack().getTrackProps().getContext().getIp();
        String accountId = String.valueOf(CampaignIdParser.parse(data.getTrack().getTrackProps().getContext().getAccount_id()));
        String messageId = data.getTrack().getTrackProps().getMessageId();
        String serverTs = data.getTrack().getTrackProps().getLemTimestamp();
        String srcId = String.valueOf(data.getTrack().getTrackProps().getContext().getSrcid());
        Validation validationObj = new Validation();

        validationObj.validate(messageId, ipAddress, accountId, serverTs, srcId);

        return this;
    }


    public void addMetrics(EventMonitoring eventMonitoring){
        eventMonitoring.addCdpMetric(String.valueOf(CampaignIdParser.parse(data.getTrack().getTrackProps().getContext().getAccount_id())),
                String.valueOf(data.getTrack().getTrackProps().getContext().getSrcid()),
                data.getTrack().getEventName(),
                EventType.TRACK,
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getTrack().getTrackProps().getContext().getAccount_id())),
                String.valueOf(data.getTrack().getTrackProps().getContext().getSrcid()),
                data.getTrack().getEventName(),
                data.getTrack().getEventProperties(),
                EventType.TRACK);

    }

    // TODO: Need to change the return type to 'TrackWeb'
    public TrackWeb build() {
        this.trackData = this.data.getTrack();
        this.trackProps = trackData.getTrackProps();
        this.context = trackProps.getContext();
        this.library = context.getLibrary();
        this.userAgent = context.getUserAgent();
        this.page  =context.getPage();
        this.otherIds = trackProps.getOtherIds();
        this.properties = trackData.getEventProperties();
        Map.Entry<Boolean, CDPCustomEventsDictionary> eventsDictionaryEntry = eventDictionaryService.getEventDictionaryData(Integer.parseInt(String.valueOf(CampaignIdParser.parse(context.getAccount_id()))), trackData.getEventName(), "track");
        this.isStandardEvent = eventsDictionaryEntry.getKey();
        this.cdpCustomEventsDictionary = eventsDictionaryEntry.getValue();

        return getTrackWebBuilder()
                .build();
    }

    private ContextPage.Builder getContextPageBuilder() {
        return ContextPage.newBuilder()
                .setPath(page.getPath())
                .setReferrer(page.getReferrer())
                .setSearch("")
                .setTitle(page.getTitle())
                .setUrl(page.getUrl());
    }

    private ContextUtm.Builder getContextUTMBuilder() {
        return ContextUtm.newBuilder()
                .setCampaign("")
                .setSource("")
                .setMedium("")
                .setTerm("")
                .setContent("");
    }

    private ContextUserAgent.Builder getContextUserAgentBuilder() {
        return ContextUserAgent.newBuilder()
                .setDeviceType(userAgent.getDeviceType())
                .setOsType(userAgent.getOsType())
                .setOsVersion(userAgent.getOsVersion())
                .setBrowser(userAgent.getBrowser())
                .setUa(userAgent.getUa());
    }

    private ContextLibrary.Builder getContextLibraryBuilder() {
        return ContextLibrary.newBuilder()
                .setName(library.getName())
                .setVersion(library.getVersion());
    }

    private Context.Builder getContextBuilder() {
        return Context.newBuilder()
                .setLibraryBuilder(getContextLibraryBuilder())
                .setPageBuilder(getContextPageBuilder())
                .setUserAgentBuilder(getContextUserAgentBuilder())
                .setIp(context.getIp())
                .setUtmBuilder(getContextUTMBuilder())
                .setSrcId(String.valueOf(context.getSrcid()))
                .setAccountId(CampaignIdParser.parse(context.getAccount_id()));
    }

    private TrackWeb.Builder getTrackWebBuilder() {
        return TrackWeb.newBuilder()
                .setId(trackProps.getId())
                .setUserId("")
                .setServerTs(trackProps.getLemTimestamp())
                .setOtherIds(otherIds)
                .setContextBuilder(getContextBuilder())
                .setEvent(trackData.getEventName())
                .setLemEvent((cdpCustomEventsDictionary != null && cdpCustomEventsDictionary.getLemEventName() != null) ? cdpCustomEventsDictionary.getLemEventName() : "")
                .setMessageId(trackProps.getMessageId())
                .setProperties(properties)
                .setReceivedAt(trackProps.getLemTimestamp())
                .setSentAt("")
                .setTimestamp(trackProps.getLemTimestamp())
                .setType(EventType.TRACK)
                .setOriginalTimestamp(trackProps.getSrcTimestamp())
                .setWriteKey(context.getWriteKey())
                .setIsStandardEvent(isStandardEvent);
    }
}
