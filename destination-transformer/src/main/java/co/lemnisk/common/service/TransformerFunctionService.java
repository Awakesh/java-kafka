package co.lemnisk.common.service;

import co.lemnisk.common.constants.EntityStatus;
import co.lemnisk.common.constants.Mode;
import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.model.*;
import co.lemnisk.common.repository.CDPPassThroughTransformersRepository;
import co.lemnisk.common.repository.CDPStandardEventsTransformersRepository;
import co.lemnisk.common.repository.CDPCustomEventsTransformersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransformerFunctionService {

    @Autowired
    TransformerCacheableData transformerCacheableData;

    @Autowired
    DestinationInstanceMappingService destinationInstanceService;

    public String getStandardTransformerFunction(String eventName, int destinationInstanceId, String eventType) {
        int destinationId = destinationInstanceService.getMappedDestinationId(destinationInstanceId);
        CDPStandardEventsTransformers standardTransformer = findStandardEventsTransformer(eventName, destinationId);

        if (Objects.nonNull(standardTransformer)) {
            return standardTransformer.getFunction();
        }
        else {
            return getPassThroughTransformerFunction(destinationId, eventType);
        }
    }

    public String getCustomTransformerFunction(String eventName, int campaignId, int destinationInstanceId, String eventType) {
        CDPCustomEventsTransformers customTransformer = findCustomEventsTransformer(campaignId, eventName, destinationInstanceId);

        if (Objects.nonNull(customTransformer)) {
            return customTransformer.getFunction();
        }
        else {
            int destinationId = destinationInstanceService.getMappedDestinationId(destinationInstanceId);
            return getPassThroughTransformerFunction(destinationId, eventType);
        }
    }

    private CDPStandardEventsTransformers findStandardEventsTransformer(String eventName, int destinationId) {
        List<CDPStandardEventsTransformers> standardTransformers = transformerCacheableData.getAllStandardEventsTransformers();

        return standardTransformers.parallelStream()
                .filter(x -> x.getEventName().equals(eventName) && x.getDestinationId().equals(destinationId))
                .findFirst()
                .orElse(null);
    }

    private CDPCustomEventsTransformers findCustomEventsTransformer(int campaignId, String eventName, int destinationInstanceId) {
        List<CDPCustomEventsTransformers> customTransformers = transformerCacheableData.getAllActiveCustomEventsTransformers();

        return customTransformers.parallelStream()
                .filter(x -> x.getCampaignId().equals(campaignId) && x.getEventName().equals(eventName) && x.getDestinationInstanceId().equals(destinationInstanceId))
                .findFirst()
                .orElse(null);
    }

    private CDPPassThroughTransformers findPassThroughEventsTransformer(int destinationId, String eventType) {
        List<CDPPassThroughTransformers> passThroughTransformers = transformerCacheableData.getAllPassThroughEventsTransformers();

        return passThroughTransformers.parallelStream()
                .filter(x -> x.getDestinationId().equals(destinationId) && x.getEventType().equals(eventType))
                .findFirst()
                .orElse(null);
    }

    private String getPassThroughTransformerFunction(int destinationId, String eventType) {
        CDPPassThroughTransformers passThroughTransformer = findPassThroughEventsTransformer(destinationId, eventType);

        if (Objects.nonNull(passThroughTransformer)) {
            return passThroughTransformer.getFunction();
        }
        else {
            throw new TransformerException("Not found any valid 'PassThrough' transformer function "+
                    "destinationId: " + destinationId +
                    ", eventType: " + eventType);
        }
    }
}

