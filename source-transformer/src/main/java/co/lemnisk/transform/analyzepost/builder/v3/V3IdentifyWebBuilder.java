package co.lemnisk.transform.analyzepost.builder.v3;

import co.lemnisk.common.avro.model.event.identify.web.*;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v3.V3IdentifyWebParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class V3IdentifyWebBuilder {

    private String rawData;
    private V3IdentifyWebParser data;
    private V3IdentifyWebParser.Context context;
    private V3IdentifyWebParser.Library library;
    private V3IdentifyWebParser.Page page;
    private V3IdentifyWebParser.UserAgent userAgent;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;

    public V3IdentifyWebBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(rawData, V3IdentifyWebParser.class);

        this.rawData = rawData;
        return this;
    }

    public V3IdentifyWebBuilder validateWithException(){
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
                EventType.IDENTIFY,
                data.getType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())),
                String.valueOf(data.getContext().getSrcId()),
                EventType.IDENTIFY,
                data.getCustomerProperties(),
                data.getType());

    }

    public IdentifyWeb build() {
        this.context= this.data.getContext();
        this.library = this.data.getContext().getLibrary();
        this.userAgent = this.data.getContext().getUserAgent();
        this.page = this.data.getContext().getPage();
        this.otherIds = this.data.getOtherIds();
        this.properties = this.data.getCustomerProperties();

        return getIdentifyWebData()
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

    private Page.Builder getPage() {
        Page.Builder pageBuilder = Page.newBuilder();
        if (page != null) {

            pageBuilder
                    .setPath(page.getPath())
                    .setReferrer(page.getReferrer())
                    .setSearch(page.getSearch())
                    .setTitle(page.getTitle())
                    .setUrl(page.getUrl());
        }
        return pageBuilder;
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
                .setUserAgentBuilder(getContextUserAgent())
                .setLibraryBuilder(getContextLibrary())
                .setIp(this.data.getContext().getIp())
                .setAccountId(CampaignIdParser.parse(data.getContext().getAccountId()))
                .setSrcId(String.valueOf(data.getContext().getSrcId()));
    }

    private IdentifyWeb.Builder getIdentifyWebData(){
        return IdentifyWeb.newBuilder()
                .setContextBuilder(getContextData())
                .setPageBuilder(getPage())
                .setId(data.getId())
                .setMessageId(data.getMessageId())
                .setOtherIds(otherIds)
                .setServerTs(data.getContext().getServer_ts())
                .setOriginalTimestamp(data.getOriginalTimestamp())
                .setCustomerProperties(properties)
                .setReceivedAt(data.getReceivedAt())
                .setTimestamp(data.getTimestamp())
                .setWriteKey(data.getWriteKey())
                .setType(EventType.IDENTIFY)
                .setSentAt(data.getSentAt())
                .setUserId(data.getUserId())
                .setIsStandardEvent(true);
    }
}
