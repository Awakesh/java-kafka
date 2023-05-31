package co.lemnisk.common.service;

import co.lemnisk.common.model.CDPSourceInstance;
import co.lemnisk.common.repository.CDPSourceInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CDPSourceInstanceService {

    @Autowired
    CDPSourceInstanceRepository cdpSourceInstanceRepository;

    @Cacheable(value = "CDPSourceInstanceCache")
    public CDPSourceInstance getCDPSourceInstance(Integer cdpSourceInstanceId) {
        return cdpSourceInstanceRepository.findById(cdpSourceInstanceId).orElse(null);
    }
}
