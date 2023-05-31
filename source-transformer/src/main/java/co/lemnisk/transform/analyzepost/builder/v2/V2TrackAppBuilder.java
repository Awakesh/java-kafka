package co.lemnisk.transform.analyzepost.builder.v2;

import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.common.avro.model.event.track.app.*;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v2.V2DataParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@NoArgsConstructor
public class V2TrackAppBuilder {

    private V2DataParser data;

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    @Autowired
    EventDictionaryService eventDictionaryService;

    public V2TrackAppBuilder(String data) throws JsonProcessingException {
        setRawData(data);
    }

    public V2TrackAppBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        data = om.readValue(rawData, V2DataParser.class);

        return this;
    }

    public TrackApp getTrackData() {

        Map<CharSequence, Object> properties = data.getProp();
        V2DataParser.Context context = data.getContext();
        Map<CharSequence,Object> otherIds = data.getOtherIds();
        Integer accountId = Integer.parseInt(String.valueOf(CampaignIdParser.parse(context.getAccount_id())));
        String eventName = (String) properties.getOrDefault("event_name", properties.getOrDefault("input_event_name", ""));
        CDPSourceInstance sourceInstance =  null;
        if (properties.get("srcid") != null){
            sourceInstance = cdpSourceInstanceService.getCDPSourceInstance(Integer.parseInt((String) properties.get("srcid")));
        }


        Map.Entry<Boolean, CDPCustomEventsDictionary> eventsDictionaryEntry = eventDictionaryService.getEventDictionaryData(accountId, eventName, "track");
        Boolean isStandardEvent = eventsDictionaryEntry.getKey();
        CDPCustomEventsDictionary cdpCustomEventsDictionary = eventsDictionaryEntry.getValue();


        ContextApp.Builder contextAppBuilder = ContextApp.newBuilder()
                .setName(context.getApp_name())
                .setVersion(context.getApp_ver())
                .setBuild(context.getApp_build());

        // TODO: Map below unmapped fields
        ContextDevice.Builder contextDeviceBuilder = ContextDevice.newBuilder()
                .setId(context.getDeviceId())
                .setAdvertisingId(context.getAdv_id())
                .setAdTrackingEnabled(true)
                .setManufacturer("")
                .setModel(context.getIos_device())
                .setType("")
                .setToken(context.getToken());

        ContextUserAgent.Builder contextUserAgentBuilder = ContextUserAgent.newBuilder()
                .setOsType(context.getEvent_sdk())
                .setOsVersion(context.getIos_version());

        ContextLibrary.Builder contextLibraryBuilder = ContextLibrary.newBuilder()
                .setName(context.getSdk_build_name())
                .setVersion(context.getSdk_build_version());

        // TODO: Map below unmapped fields
        ContextScreen.Builder contextScreenBuilder = ContextScreen.newBuilder()
                .setWidth("")
                .setHeight("")
                .setDensity("");

        Context.Builder contextBuilder = Context.newBuilder()
                .setLibraryBuilder(contextLibraryBuilder)
                .setAppBuilder(contextAppBuilder)
                .setDeviceBuilder(contextDeviceBuilder)
                .setScreenBuilder(contextScreenBuilder)
                .setUserAgentBuilder(contextUserAgentBuilder)
                .setIp(context.getIp())
                .setSrcId(String.valueOf(properties.getOrDefault("srcid", context.getSrcid())))
                .setAccountId(CampaignIdParser.parse(context.getAccount_id()));

        return TrackApp.newBuilder()
                .setId(context.getVizid())
                .setServerTs(context.getServer_ts())
                .setUserId((CharSequence) properties.getOrDefault("userId", ""))
                .setOtherIds(otherIds)
                .setContextBuilder(contextBuilder)
                .setEvent((CharSequence) properties.getOrDefault("event_name", properties.getOrDefault("input_event_name", "")))
                .setLemEvent((cdpCustomEventsDictionary != null && cdpCustomEventsDictionary.getLemEventName() != null) ? cdpCustomEventsDictionary.getLemEventName() : "")
                .setMessageId(context.getMsgId())
                .setProperties(properties)
                .setReceivedAt(data.getReceivedAt())
                .setSentAt(context.getSentAt())
                .setTimestamp((CharSequence) properties.getOrDefault("timestamp", ""))
                .setType(EventType.TRACK)
                .setOriginalTimestamp((CharSequence) properties.getOrDefault("originalTimestamp", ""))
                .setWriteKey(sourceInstance != null ? sourceInstance.getWriteKey() : "")
                .setIsStandardEvent(isStandardEvent)
                .build();
    }
}

