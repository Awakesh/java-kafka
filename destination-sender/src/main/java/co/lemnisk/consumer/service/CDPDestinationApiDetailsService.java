package co.lemnisk.consumer.service;

import co.lemnisk.consumer.entity.CDPDestinationApiDetails;
import co.lemnisk.consumer.repo.CDPDestinationApiDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CDPDestinationApiDetailsService {
    @Autowired
    CDPDestinationApiDetailsRepo cdpDestinationApiDetailsRepo;
    @Cacheable(value = "cdpDestinationAPI")
    public CDPDestinationApiDetails getCDPDestinationApiDetails(Integer cdpDestinationId){
        return cdpDestinationApiDetailsRepo.findByCdpDestinationId(cdpDestinationId);
    }
}
