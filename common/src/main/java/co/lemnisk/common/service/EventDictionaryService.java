package co.lemnisk.common.service;

import co.lemnisk.common.constants.EntityStatus;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPStandardEventDictionary;
import co.lemnisk.common.model.CDPStandardEventsPropsCampaignMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EventDictionaryService {

    @Autowired
    CommonCacheableData commonCacheableData;

    private CDPCustomEventsDictionary getCustomEventMapping(Integer campaignId, String eventName) {
        List<CDPCustomEventsDictionary> customEventsData = commonCacheableData.getAllActiveCustomEventsData();

        return customEventsData.parallelStream()
                .filter(x -> x.getEventName().equals(eventName) && x.getCampaignId().equals(campaignId))
                .findFirst()
                .orElse(null);
    }

    private CDPStandardEventsPropsCampaignMapping getStandardEventMappingInfo(Integer campaignId, String eventName, String eventType) {
        List<CDPStandardEventDictionary> standardEventsData = commonCacheableData.getAllStandardEventsData();

        return standardEventsData.parallelStream()
                .filter(standardEvent -> standardEvent.getEventName().equals(eventName) && standardEvent.getEventType().equals(eventType))
                .map(standardEvent -> standardEvent.getCdpStandardEventsPropsCampaignMappings().parallelStream()
                        .filter(eventProps -> eventProps.getCampaignId().equals(campaignId) && eventProps.getIsActive().equals(EntityStatus.ACTIVE))
                        .findFirst()
                        .orElse(null)
                )
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Map.Entry<Boolean, CDPCustomEventsDictionary> getEventDictionaryData(int accountId, String eventName, String eventType) {
        if(Objects.nonNull(getStandardEventMappingInfo(accountId, eventName, eventType))) {
            return new AbstractMap.SimpleEntry<>(true, null); // as it is standard event then there will be no lemEventName and no possibility of entry in customEventDictionary
        }
        else {
            var customEventData = getCustomEventMapping(accountId, eventName);
            if(Objects.nonNull(customEventData)) {
                return new AbstractMap.SimpleEntry<>(false, customEventData);
            }
            else {
                throw new TransformerException("Event is neither standard nor custom ==> {" +
                        "accountId:" + accountId + ", " +
                        "eventName: " + eventName + ", " +
                        "eventType: " + eventType + " }");
            }
        }
    }

    public String getAllowedDestInstanceList(Integer campaignId, String eventName, String eventType, boolean isStandardEvent) {
        if(isStandardEvent) {
            var standardEventMappingData = getStandardEventMappingInfo(campaignId, eventName, eventType);
            if (Objects.nonNull(standardEventMappingData))
                return standardEventMappingData.getAllowedDestinationInstanceList();
            else
                return null;
        }
        else {
            var customEventMappingData = getCustomEventMapping(campaignId, eventName);
            if (Objects.nonNull(customEventMappingData))
                return customEventMappingData.getAllowedDestinationInstanceList();
            else
                return null;
        }
    }
}
