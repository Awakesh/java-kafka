package co.lemnisk.transform.analyzepost.builder.v2;

import co.lemnisk.common.avro.model.event.identify.app.*;
import co.lemnisk.common.constants.EventType;
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
public class V2IdentifyAppBuilder {

    private V2DataParser data;

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    public V2IdentifyAppBuilder(String data) throws JsonProcessingException {
        setRawData(data);
    }

    public V2IdentifyAppBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        data = om.readValue(rawData, V2DataParser.class);

        return this;
    }

    public IdentifyApp getIdentifyData() {

        Map<CharSequence, Object> properties = data.getProp();
        V2DataParser.Context context = data.getContext();
        Map<CharSequence,Object> otherIds = data.getOtherIds();
        CDPSourceInstance sourceInstance =  null;
        if (properties.get("srcid") != null){
             sourceInstance = cdpSourceInstanceService.getCDPSourceInstance(Integer.parseInt((String) properties.get("srcid")));
        }

        ContextApp.Builder contextAppBuilder = ContextApp.newBuilder()
                .setName(context.getApp_name())
                .setVersion(context.getApp_ver())
                .setBuild(context.getApp_build());

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

        return IdentifyApp.newBuilder()
                .setId(context.getVizid())
                .setServerTs(context.getServer_ts())
                .setUserId((CharSequence) properties.getOrDefault("userId", ""))
                .setOtherIds(otherIds)
                .setContextBuilder(contextBuilder)
                .setMessageId(context.getMsgId())
                .setCustomerProperties(properties)
                .setReceivedAt(data.getReceivedAt())
                .setSentAt(context.getSentAt())
                .setTimestamp((CharSequence) properties.getOrDefault("timestamp", ""))
                .setType(EventType.IDENTIFY)
                .setOriginalTimestamp((CharSequence) properties.getOrDefault("originalTimestamp", ""))
                .setWriteKey(sourceInstance != null ? sourceInstance.getWriteKey() : "")
                .setIsStandardEvent(true)
                .build();
    }
}
