package co.lemnisk.transform.analyzepost.builder.v3;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.avro.model.event.track.app.*;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v3.V3TrackAppParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@NoArgsConstructor
public class V3TrackAppBuilder {

    private String rawData;
    private V3TrackAppParser data;
    private V3TrackAppParser.Context context;
    private V3TrackAppParser.Library library;
    private V3TrackAppParser.App app;
    private V3TrackAppParser.Screen screen;
    private V3TrackAppParser.UserAgent userAgent;
    private V3TrackAppParser.Device device;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;
    private Boolean isStandardEvent;
    private CDPCustomEventsDictionary cdpCustomEventsDictionary;

    @Autowired
    EventDictionaryService eventDictionaryService;

    public V3TrackAppBuilder(String rawData) throws JsonProcessingException {
        setRawData(rawData);
    }

    public V3TrackAppBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(rawData, V3TrackAppParser.class);
        this.rawData = rawData;
        return this;
    }

    public V3TrackAppBuilder validateWithException(){
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

    public TrackApp build() {

        this.context= this.data.getContext();
        this.library = this.data.getContext().getLibrary();
        this.userAgent = this.data.getContext().getUserAgent();
        this.device = this.data.getContext().getDevice();
        this.app = this.data.getContext().getApp();
        this.screen = this.data.getContext().getScreen();
        this.otherIds = this.data.getOtherIds();
        this.properties = this.data.getProperties();

        constructEventMappingData();

        return getTrackData()
                .build();
    }

    private ContextUserAgent.Builder getContextUserAgent(){
        ContextUserAgent.Builder userAgentBuilder = ContextUserAgent.newBuilder();
        if (userAgent != null) {

            userAgentBuilder
                    .setOsType(userAgent.getOsType())
                    .setOsVersion(userAgent.getOsVersion());
        }
        return userAgentBuilder;
    }

    private ContextDevice.Builder getContextDevice(){
        ContextDevice.Builder deviceBuilder = ContextDevice.newBuilder();
        if(device != null) {

            deviceBuilder
                    .setToken(device.getToken())
                    .setManufacturer(device.getManufacturer())
                    .setAdTrackingEnabled(device.isAdTrackingEnabled())
                    .setType(device.getType())
                    .setId(device.getId())
                    .setAdvertisingId(device.getAdvertisingId())
                    .setModel("");
        }
        return deviceBuilder;
    }

    private ContextApp.Builder getContextApp() {
        ContextApp.Builder appBuilder = ContextApp.newBuilder();
        if (app != null) {
            appBuilder
                    .setBuild(app.getBuild())
                    .setName(app.getName())
                    .setVersion(app.getVersion());
        }
        return appBuilder;
    }

    private ContextScreen.Builder getContextScreen() {
        ContextScreen.Builder screenBuilder = ContextScreen.newBuilder();
        if (screen != null) {

            screenBuilder
                    .setDensity(screen.getDensity())
                    .setHeight(screen.getHeight())
                    .setWidth(screen.getWidth());
        }
        return screenBuilder;
    }

    private ContextLibrary.Builder getContextLibrary(){
        ContextLibrary.Builder libraryBuilder = ContextLibrary.newBuilder();

        if (library != null) {
            libraryBuilder
                    .setName(library.getName())
                    .setVersion(library.getVersion());
        }
        return libraryBuilder;
    }

    private Context.Builder getContextData(){
        return Context.newBuilder()
                .setAppBuilder(getContextApp())
                .setDeviceBuilder(getContextDevice())
                .setScreenBuilder(getContextScreen())
                .setUserAgentBuilder(getContextUserAgent())
                .setLibraryBuilder(getContextLibrary())
                .setIp(this.data.getContext().getIp())
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

    private TrackApp.Builder getTrackData(){
        return TrackApp.newBuilder()
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
