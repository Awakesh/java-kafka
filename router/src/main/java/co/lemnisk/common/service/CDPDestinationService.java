package co.lemnisk.common.service;

import co.lemnisk.common.repository.CDPDestinationInstanceRepository;
import co.lemnisk.common.repository.CDPDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CDPDestinationService {

    @Autowired
    CDPDestinationRepository cdpDestinationRepository;

    @Autowired
    CDPDestinationInstanceRepository cdpDestinationInstanceRepository;

    @Cacheable(value = "LivenessDuration")
    public Integer getLivenessDuration(Integer id){

        Integer outputDestinationId = cdpDestinationInstanceRepository.findById(id).get().getCdpDestinationId();

        return cdpDestinationRepository.findById(outputDestinationId).get().getLiveness();
    }

}

