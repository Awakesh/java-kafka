package co.lemnisk.common.service;

import co.lemnisk.common.constants.Constants;
import co.lemnisk.common.constants.EntityStatus;
import co.lemnisk.common.constants.Status;
import co.lemnisk.common.model.CDPCustomEventsDictionary;
import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.model.CDPStandardEventDictionary;
import co.lemnisk.common.repository.CDPCustomEventsDictionaryRepository;
import co.lemnisk.common.repository.CDPDestinationInstanceRepository;
import co.lemnisk.common.repository.CDPStandardEventsDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonCacheableData {
    @Autowired
    CDPCustomEventsDictionaryRepository cdpCustomEventsDictionaryRepo;

    @Autowired
    CDPStandardEventsDictionaryRepository cdpStandardEventsDictionaryRepo;

    @Autowired
    CDPDestinationInstanceRepository cdpDestinationInstanceRepo;

    @Cacheable(value = "customEventsCache")
    public List<CDPCustomEventsDictionary> getAllActiveCustomEventsData() {
        return cdpCustomEventsDictionaryRepo.findByIsActive(EntityStatus.ACTIVE);
    }

    @Cacheable(value = "standardEventsCache")
    public List<CDPStandardEventDictionary> getAllStandardEventsData() {
        return cdpStandardEventsDictionaryRepo.findAll();
    }

    @Cacheable(value = "destinationInstancesCache")
    public List<CDPDestinationInstance> getAllActiveDestinationInstances() {
        return cdpDestinationInstanceRepo.findByStatusAndMode(Status.ACTIVE, Constants.CLOUD_MODE);
    }
}
