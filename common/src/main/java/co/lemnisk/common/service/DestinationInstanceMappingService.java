package co.lemnisk.common.service;

import co.lemnisk.common.exception.TransformerException;
import co.lemnisk.common.model.CDPDestinationInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DestinationInstanceMappingService {
    @Autowired
    CommonCacheableData commonCacheableData;

    public List<CDPDestinationInstance> getAllActiveDestinationInstances() {
        return commonCacheableData.getAllActiveDestinationInstances();
    }

    public Integer getMappedDestinationId(int destinationInstanceId) {
        CDPDestinationInstance mappedDestInstance = getDestinationInstanceEntity(destinationInstanceId);
        if (Objects.nonNull(mappedDestInstance)) {
            return mappedDestInstance.getCdpDestinationId();
        }
        else {
            throw new TransformerException("It is not an active destination-instance (DestinationInstanceId: " + destinationInstanceId + ")");
        }
    }

    public CDPDestinationInstance getDestinationInstanceEntity(int destinationInstanceId) {
        List<CDPDestinationInstance> allActiveDestInstances = getAllActiveDestinationInstances();
        return allActiveDestInstances.parallelStream()
                .filter(x -> x.getId().equals(destinationInstanceId))
                .findFirst()
                .orElse(null);
    }
}
