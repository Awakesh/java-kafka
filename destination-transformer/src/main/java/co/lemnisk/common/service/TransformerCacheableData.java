package co.lemnisk.common.service;

import co.lemnisk.common.constants.EntityStatus;
import co.lemnisk.common.constants.Mode;
import co.lemnisk.common.model.CDPCustomEventsTransformers;
import co.lemnisk.common.model.CDPPassThroughTransformers;
import co.lemnisk.common.model.CDPStandardEventsTransformers;
import co.lemnisk.common.repository.CDPCustomEventsTransformersRepository;
import co.lemnisk.common.repository.CDPPassThroughTransformersRepository;
import co.lemnisk.common.repository.CDPStandardEventsTransformersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransformerCacheableData {
    @Autowired
    CDPCustomEventsTransformersRepository customEventsTransformersRepo;

    @Autowired
    CDPStandardEventsTransformersRepository standardEventsTransformersRepo;

    @Autowired
    CDPPassThroughTransformersRepository passThroughTransformersRepo;

    @Cacheable(value = "standardEventsTransformersCache")
    public List<CDPStandardEventsTransformers> getAllStandardEventsTransformers() {
        return standardEventsTransformersRepo.findAllByMode(Mode.cloud);
    }

    @Cacheable(value = "customEventsTransformersCache")
    public List<CDPCustomEventsTransformers> getAllActiveCustomEventsTransformers() {
        return customEventsTransformersRepo.findByIsActive(EntityStatus.ACTIVE);
    }

    @Cacheable(value = "passThroughEventsTransformersCache")
    public List<CDPPassThroughTransformers> getAllPassThroughEventsTransformers() {
        return passThroughTransformersRepo.findAllByMode(Mode.cloud);
    }
}
