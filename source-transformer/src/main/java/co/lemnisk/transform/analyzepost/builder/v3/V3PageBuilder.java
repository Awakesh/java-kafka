package co.lemnisk.transform.analyzepost.builder.v3;

import co.lemnisk.common.avro.model.event.page.*;
import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.metrics.EventMonitoring;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.analyzepost.model.parser.v3.V3PageParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class V3PageBuilder {

    private String rawData;
    private V3PageParser data;
    private V3PageParser.Context context;
    private V3PageParser.Library library;
    private V3PageParser.UserAgent userAgent;
    private V3PageParser.Utm utm;
    private Map<CharSequence, Object> otherIds;
    private Map<CharSequence, Object> properties;

    public V3PageBuilder setRawData(String rawData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.data = objectMapper.readValue(rawData, V3PageParser.class);
        this.rawData = rawData;
        return this;
    }

    public V3PageBuilder validateWithException(){
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
                EventType.PAGE_VIEW,
                data.getType(),
                Constants.ModuleNames.SOURCE_TRANSFORMER);

        eventMonitoring.addCdpEventPropertiesMetric(String.valueOf(CampaignIdParser.parse(data.getContext().getAccountId())),
                String.valueOf(data.getContext().getSrcId()),
                EventType.PAGE_VIEW,
                data.getProperties(),
                data.getType());

    }

    public Page build() {
        this.context= this.data.getContext();
        this.library = this.data.getContext().getLibrary();
        this.userAgent = this.data.getContext().getUserAgent();
        this.utm = this.data.getContext().getUtm();
        this.otherIds = this.data.getOtherIds();
        this.properties = this.data.getProperties();

        return getPageData()
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
                .setIp(this.data.getContext().getIp())
                .setUtmBuilder(getContextUtm())
                .setLibraryBuilder(getContextLibrary())
                .setAccountId(CampaignIdParser.parse(data.getContext().getAccountId()))
                .setSrcId(String.valueOf(data.getContext().getSrcId()));
    }

    private Page.Builder getPageData(){
        return Page.newBuilder()
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
                .setType(EventType.PAGE)
                .setSentAt(data.getSentAt())
                .setUserId(data.getUserId())
                .setIsStandardEvent(true);
    }
}
