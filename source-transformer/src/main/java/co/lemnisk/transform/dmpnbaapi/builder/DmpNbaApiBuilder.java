package co.lemnisk.transform.dmpnbaapi.builder;


import co.lemnisk.common.avro.model.event.dmpnba.Context;
import co.lemnisk.common.avro.model.event.dmpnba.DmpNba;
import co.lemnisk.common.constants.EventType;
import co.lemnisk.common.constants.SourceTransformerConstant;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.service.CDPSourceInstanceService;
import co.lemnisk.common.service.EventDictionaryService;
import co.lemnisk.transform.CampaignIdParser;
import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiInputPayload;
import co.lemnisk.transform.dmpnbaapi.model.DmpNbaApiParser;
import co.lemnisk.validation.Validation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;


public  class DmpNbaApiBuilder{

    DmpNbaApiParser dmpNbaApiParser;
    @Getter
    private final String rawData;

    private final DmpNbaApiInputPayload dmpNbaApiInputPayload;

    @Autowired
    EventDictionaryService eventDictionaryService;

    @Autowired
    CDPSourceInstanceService cdpSourceInstanceService;

    public DmpNbaApiBuilder(DmpNbaApiInputPayload dmpNbaApiInputPayload, String rawData) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        dmpNbaApiParser = new DmpNbaApiParser();
        dmpNbaApiParser.setData(om.readValue(dmpNbaApiInputPayload.getData(), DmpNbaApiParser.Data.class));
        dmpNbaApiParser.setCampaignId(dmpNbaApiInputPayload.getCampaignId());

        this.rawData = rawData;
        this.dmpNbaApiInputPayload = dmpNbaApiInputPayload;
    }

    public boolean isValidEvent() {
        return EventType.IDENTIFY.equals(eventType()) || EventType.TRACK.equals(eventType());
    }

    public String eventType() {
        return dmpNbaApiParser.getData().getType();
    }

    public void isValidData() {
        String accountId = String.valueOf(CampaignIdParser.parse(dmpNbaApiInputPayload.getCampaignId()));
        String srcId = dmpNbaApiParser.getData().getSrcid();
        String serverTs = dmpNbaApiInputPayload.getServerTs();

        Validation validationObj = new Validation();
        validationObj.validateDmpNba(srcId, serverTs, accountId) ;
    }

    public DmpNba buildEventData() {
        int campaignId = Integer.parseInt(String.valueOf(CampaignIdParser.parse(dmpNbaApiParser.getCampaignId())));

        Map.Entry<Boolean, CDPCustomEventsDictionary> eventsDictionaryEntry = eventDictionaryService.getEventDictionaryData(campaignId, getEventName(), eventType());
        boolean isStandardEvent = eventsDictionaryEntry.getKey();
        CDPCustomEventsDictionary cdpCustomEventsDictionary = eventsDictionaryEntry.getValue();

        return DmpNba.newBuilder()
                .setContextBuilder(getContext())
                .setType(eventType())
                .setEvent(getEventName())
                .setLemEvent((cdpCustomEventsDictionary != null && cdpCustomEventsDictionary.getLemEventName() != null) ? cdpCustomEventsDictionary.getLemEventName() : "")
                .setMessageId(messageId())
                .setProperties(getProperties())
                .setUserId(dmpNbaApiParser.getData().getUserid())
                .setIsStandardEvent(isStandardEvent)
                .build();

    }

    public Map<CharSequence, Object> getProperties() {
        return dmpNbaApiParser.getData().getProperties();
    }

    public String getEventName() {
        switch (eventType()) {
            case EventType.IDENTIFY:
                return EventType.IDENTIFY;
            case EventType.PAGE:
                return EventType.PAGE;
            case EventType.SCREEN:
                return EventType.SCREEN;
            case EventType.TRACK:
                return dmpNbaApiParser.getData().getEventname();
            default:
                return EventType.UNKNOWN;
        }
    }

    private String messageId() {
        String messageId = dmpNbaApiParser.getData().getId();

        if(StringUtils.isBlank(messageId))
            messageId = UUID.randomUUID().toString();

        return messageId;
    }

    public Context.Builder getContext(){
        String srcId = dmpNbaApiParser.getData().getSrcid();
        String writeKey = dmpNbaApiParser.getData().getWriteKey();

        if (StringUtils.isNotEmpty(srcId) && StringUtils.isEmpty(writeKey)) {
            CDPSourceInstance sourceInstance = cdpSourceInstanceService.getCDPSourceInstance(Integer.parseInt(srcId));
            writeKey = (sourceInstance != null ? sourceInstance.getWriteKey() : "");
        }

        return Context.newBuilder()
                .setSrcId(srcId)
                .setAccountId(CampaignIdParser.parse(dmpNbaApiParser.getCampaignId()))
                .setOtherIds(dmpNbaApiParser.getData().getOtherIds())
                .setServerTs(rawData.split(SourceTransformerConstant.PAYLOAD_SEPARATOR)[1])
                .setWriteKey(writeKey);
    }

}

