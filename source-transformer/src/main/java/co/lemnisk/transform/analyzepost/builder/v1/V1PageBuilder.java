package co.lemnisk.transform.analyzepost.builder.v1;

import co.lemnisk.common.avro.model.event.page.*;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v1.V1PageDataParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class V1PageBuilder {

    private V1PageDataParser data = null;
    private V1PageDataParser.Page pageData;
    private V1PageDataParser.Page.Context context;
    private V1PageDataParser.Page.Context.Library library;
    private V1PageDataParser.Page.Context.UserAgent userAgent;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;

    public V1PageBuilder(String inputJsonStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(inputJsonStr, V1PageDataParser.class);
    }

    public V1PageBuilder validateWithException() {
        String ipAddress = data.getPage().getContext().getIp();
        String accountId = String.valueOf(CampaignIdParser.parse(data.getPage().getContext().getAccount_id()));
        String messageId = data.getPage().getMessageId();
        String serverTs = data.getPage().getLemTimestamp();
        String srcId = String.valueOf(data.getPage().getContext().getSrcid());
        Validation validationObj = new Validation();
        validationObj.validate(messageId, ipAddress, accountId, serverTs, srcId);

        return this;
    }

    public void addMetrics(EventMonitoring eventMonitoring){
        eventMonitoring.addCdpMetric(String.valueOf(CampaignIdParser.parse(data.getPage().getContext().getAccount_id())),
                String.valueOf(data.getPage().getContext().getSrcid()),
                EventType.PAGE_VIEW,
                EventType.PAGE,
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getPage().getContext().getAccount_id())),
                String.valueOf(data.getPage().getContext().getSrcid()),
                EventType.PAGE_VIEW,
                data.getPage().getPageProps(),
                EventType.PAGE);

    }

    public Page build() {
        this.pageData = this.data.getPage();
        this.context = pageData.getContext();
        this.library = context.getLibrary();
        this.userAgent = context.getUserAgent();
        this.otherIds = pageData.getOtherIds();
        this.properties = pageData.getPageProps();

        return getPageBuilder()
                .build();
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
                .setUserAgentBuilder(getContextUserAgentBuilder())
                .setIp(context.getIp())
                .setUtmBuilder(getContextUTMBuilder())
                .setSrcId(String.valueOf(context.getSrcid()))
                .setAccountId(CampaignIdParser.parse(context.getAccount_id()));
    }

    private Page.Builder getPageBuilder() {
        return Page.newBuilder()
                .setId(pageData.getId())
                .setUserId("")
                .setServerTs(pageData.getLemTimestamp())
                .setOtherIds(otherIds)
                .setContextBuilder(getContextBuilder())
                .setName((CharSequence) properties.getOrDefault("name", ""))
                .setMessageId(pageData.getMessageId())
                .setProperties(properties)
                .setReceivedAt(pageData.getLemTimestamp())
                .setSentAt("")
                .setTimestamp(pageData.getLemTimestamp())
                .setType(EventType.PAGE)
                .setOriginalTimestamp(pageData.getSrcTimestamp())
                .setWriteKey(context.getWriteKey())
                .setIsStandardEvent(true);
    }
}
