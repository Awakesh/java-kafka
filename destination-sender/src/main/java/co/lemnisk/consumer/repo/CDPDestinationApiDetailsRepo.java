package co.lemnisk.consumer.repo;

import co.lemnisk.consumer.entity.CDPDestinationApiDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDPDestinationApiDetailsRepo extends JpaRepository<CDPDestinationApiDetails, Integer> {
    public CDPDestinationApiDetails findByCdpDestinationId(Integer cdpDestinationId);
}
