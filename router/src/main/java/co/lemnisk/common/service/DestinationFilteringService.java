package co.lemnisk.common.service;

import co.lemnisk.common.constants.Status;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.repository.CDPSourceDestinationInstanceMappingRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DestinationFilteringService {

    @Autowired
    EventDictionaryService eventDictionaryService;

    @Autowired
    RouterCacheableData routerCacheableData;

    public Set<Integer> getDestinationInstanceIds(Integer campaignId, String eventName, String eventType, Integer srcId, boolean isStandardEvent) {

        String allowedDestination = eventDictionaryService.getAllowedDestInstanceList(campaignId, eventName, eventType, isStandardEvent);
        List<Integer> allowedDestList = null;

        if(allowedDestination != null) {
           allowedDestList = new ArrayList<>();
            String[] stringArr = allowedDestination.replaceAll("[\\[\\](){}\"]", "")
                    .split(",");

            for (String str : stringArr) {
                if(StringUtils.isNotEmpty(str)) {
                    allowedDestList.add(Integer.valueOf(str));
                }
            }
        }

        List<Integer> srcDestInstanceMapping = routerCacheableData.getActiveMappedCDPDestinationInstanceIds(srcId);

        Set<Integer> activeCloudModeDestinationInstances = routerCacheableData.getCloudModeDestinationInstanceIds();

        Set<Integer> filterDestinationList = null;

        if(Objects.nonNull(srcDestInstanceMapping) && Objects.nonNull(allowedDestList)) {
            filterDestinationList = allowedDestList.stream()
                    .distinct()
                    .filter(o -> srcDestInstanceMapping.contains(o) && activeCloudModeDestinationInstances.contains(o))
                    .collect(Collectors.toSet());
        }

        if (Objects.isNull(filterDestinationList) || filterDestinationList.isEmpty()) {

            throw new TransformerException("No valid destination instance is found. " +
                    "campaignId: " + campaignId +
                    ", eventName: " + eventName +
                    ", eventType: " + eventType +
                    ", srcId: " + srcId);
        }

        return filterDestinationList;
    }
}
