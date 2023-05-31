package co.lemnisk.consumer.service;

import co.lemnisk.consumer.entity.CDPDestination;
import co.lemnisk.consumer.repo.CDPDestinationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CDPDestinationService {

    @Autowired
    CDPDestinationRepo cdpDestinationRepo;

    public Optional<CDPDestination> getDetails(Long id){
        return cdpDestinationRepo.findById(id);
    }
}
