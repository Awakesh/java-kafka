package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.avro.model.event.identify.web.*;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v1.V1IdentifyWebDataParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class V1IdentifyWebBuilder {

    private V1IdentifyWebDataParser data = null;
    private V1IdentifyWebDataParser.Identify identifyData;
    private V1IdentifyWebDataParser.Identify.IdentifyProps identifyProps;
    private V1IdentifyWebDataParser.Identify.IdentifyProps.Context context;
    private V1IdentifyWebDataParser.Identify.IdentifyProps.Context.Library library;
    private V1IdentifyWebDataParser.Identify.IdentifyProps.Context.UserAgent userAgent;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;

    public V1IdentifyWebBuilder(String inputJsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        this.data = objectMapper.readValue(inputJsonStr, V1IdentifyWebDataParser.class);
    }

    public V1IdentifyWebBuilder validateWithException() {
        String ipAddress = data.getIdentify().getIdentifyProps().getContext().getIp();
        String accountId = String.valueOf(CampaignIdParser.parse(data.getIdentify().getIdentifyProps().getContext().getAccount_id()));
        String messageId = data.getIdentify().getIdentifyProps().getMessageId();
        String serverTs = data.getIdentify().getIdentifyProps().getLemTimestamp();
        String srcId = String.valueOf(data.getIdentify().getIdentifyProps().getContext().getSrcid());
        Validation validationObj = new Validation();

        validationObj.validate(messageId, ipAddress, accountId,serverTs, srcId);

        return this;
    }

    public void addMetrics(EventMonitoring eventMonitoring){
        eventMonitoring.addCdpMetric(String.valueOf(CampaignIdParser.parse(data.getIdentify().getIdentifyProps().getContext().getAccount_id())),
                String.valueOf(data.getIdentify().getIdentifyProps().getContext().getSrcid()),
                EventType.IDENTIFY,
                EventType.IDENTIFY,
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getIdentify().getIdentifyProps().getContext().getAccount_id())),
                String.valueOf(data.getIdentify().getIdentifyProps().getContext().getSrcid()),
                EventType.IDENTIFY,
                data.getIdentify().getProperties(),
                EventType.IDENTIFY);

    }

    public IdentifyWeb build() {
        this.identifyData = this.data.getIdentify();
        this.identifyProps = identifyData.getIdentifyProps();
        this.context = identifyProps.getContext();
        this.library = context.getLibrary();
        this.userAgent = context.getUserAgent();
        this.otherIds = identifyProps.getOtherIds();
        this.properties = identifyData.getProperties();

        return getIdentifyWebBuilder()
                .build();
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
                .setUserAgentBuilder(getContextUserAgentBuilder())
                .setIp(context.getIp())
                .setSrcId(String.valueOf(context.getSrcid()))
                .setAccountId(CampaignIdParser.parse(context.getAccount_id()));
    }

    private Page.Builder getPageBuilder() {
        return Page.newBuilder()
                .setPath("")
                .setReferrer("")
                .setSearch("")
                .setTitle("")
                .setUrl("");
    }

    private IdentifyWeb.Builder getIdentifyWebBuilder() {
        return IdentifyWeb.newBuilder()
                .setId(identifyProps.getId())
                .setUserId("")
                .setServerTs(identifyProps.getLemTimestamp())
                .setOtherIds(otherIds)
                .setContextBuilder(getContextBuilder())
                .setPageBuilder(getPageBuilder())
                .setMessageId(identifyProps.getMessageId())
                .setCustomerProperties(properties)
                .setReceivedAt(identifyProps.getLemTimestamp())
                .setSentAt("")
                .setTimestamp(identifyProps.getLemTimestamp())
                .setType(EventType.IDENTIFY)
                .setOriginalTimestamp(identifyProps.getSrcTimestamp())
                .setWriteKey(context.getWriteKey())
                .setIsStandardEvent(true);
    }
}
