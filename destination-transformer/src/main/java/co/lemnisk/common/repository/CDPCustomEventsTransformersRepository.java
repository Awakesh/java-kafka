package co.lemnisk.common.repository;

import co.lemnisk.common.model.CDPCustomEventsTransformers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPCustomEventsTransformersRepository extends JpaRepository<CDPCustomEventsTransformers, Integer> {
    public List<CDPCustomEventsTransformers> findByIsActive(int status);
}

