package co.lemnisk.common.repository;

import co.lemnisk.common.model.CDPSourceInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDPSourceInstanceRepository extends JpaRepository<CDPSourceInstance, Integer> {
}
