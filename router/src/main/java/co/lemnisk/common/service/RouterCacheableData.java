package co.lemnisk.common.service;

import co.lemnisk.common.model.CDPDestinationInstance;
import co.lemnisk.common.repository.CDPSourceDestinationInstanceMappingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RouterCacheableData {

    @Autowired
    CommonCacheableData commonCacheableData;

    @Autowired
    CDPSourceDestinationInstanceMappingRepository sourceDestInstanceMappingRepo;

    @Cacheable(value = "activeCloudModeDestinationInstanceIds")
    public Set<Integer> getCloudModeDestinationInstanceIds() {
        return commonCacheableData.getAllActiveDestinationInstances().stream()
                .map(CDPDestinationInstance::getId).collect(Collectors.toSet());
    }

    @Cacheable(value = "activeMappedCDPDestinationInstanceIds")
    public List<Integer> getActiveMappedCDPDestinationInstanceIds(Integer srcId) {
      return sourceDestInstanceMappingRepo.getActiveMappedCDPDestinationInstanceIds(srcId);
    }

}
