package co.lemnisk.common.repository;

import co.lemnisk.common.constants.Status;
import co.lemnisk.common.model.CDPDestinationInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPDestinationInstanceRepository extends JpaRepository<CDPDestinationInstance, Integer> {
    List<CDPDestinationInstance> findByStatus(Status active);

    List<CDPDestinationInstance> findByStatusAndMode(Status active, String cloud);
}
