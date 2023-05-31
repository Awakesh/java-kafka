package co.lemnisk.transform.analyzepost.builder.v3;


import co.lemnisk.common.avro.model.event.screen.*;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v3.V3ScreenParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class V3ScreenBuilder {

    private String rawData;
    private V3ScreenParser data;
    private V3ScreenParser.Context context;
    private V3ScreenParser.Library library;
    private V3ScreenParser.App app;
    private V3ScreenParser.Screen screen;
    private V3ScreenParser.UserAgent userAgent;
    private V3ScreenParser.Device device;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;

    public V3ScreenBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.data = objectMapper.readValue(rawData, V3ScreenParser.class);
        this.rawData = rawData;
        return this;
    }

    public V3ScreenBuilder validateWithException(){
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
                EventType.SCREEN,
                data.getType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())),
                String.valueOf(data.getContext().getSrcId()),
                EventType.SCREEN,
                data.getProperties(),
                data.getType());

    }

    public Screen build() {
        this.context= this.data.getContext();
        this.library = this.data.getContext().getLibrary();
        this.userAgent = this.data.getContext().getUserAgent();
        this.device = this.data.getContext().getDevice();
        this.app = this.data.getContext().getApp();
        this.screen = this.data.getContext().getScreen();
        this.otherIds = this.data.getOtherIds();
        this.properties = this.data.getProperties();

        return getScreenData()
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

    private Screen.Builder getScreenData(){
        return Screen.newBuilder()
                .setContextBuilder(getContextData())
                .setId(data.getId())
                .setMessageId(data.getMessageId())
                .setOtherIds(otherIds)
                .setServerTs(data.getContext().getServer_ts())
                .setOriginalTimestamp(data.getOriginalTimestamp())
                .setProperties(properties)
                .setReceivedAt(data.getReceivedAt())
                .setTimestamp(data.getTimestamp())
                .setWriteKey(data.getWriteKey())
                .setType(EventType.SCREEN)
                .setSentAt(data.getSentAt())
                .setUserId(data.getUserId())
                .setIsStandardEvent(true);
    }

}
