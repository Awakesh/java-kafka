package co.lemnisk.common.repository;

import co.lemnisk.common.constants.Status;
import co.lemnisk.common.model.CDPSourceDestinationInstanceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPSourceDestinationInstanceMappingRepository extends JpaRepository<CDPSourceDestinationInstanceMapping, Integer> {

    @Query(value = "select cdp.CDPDestinationInstanceId " +
            "from CDPSourceDestinationInstanceMapping cdp " +
            "where cdp.CDPSourceInstanceId = ?1 and cdp.Status = 'ACTIVE'",
            nativeQuery = true)
    List<Integer> getActiveMappedCDPDestinationInstanceIds(Integer srcId);
}
