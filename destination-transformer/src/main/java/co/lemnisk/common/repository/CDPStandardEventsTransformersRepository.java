package co.lemnisk.common.repository;


import co.lemnisk.common.model.CDPStandardEventsTransformers;
import co.lemnisk.common.constants.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPStandardEventsTransformersRepository extends JpaRepository<CDPStandardEventsTransformers,Integer> {
    List<CDPStandardEventsTransformers> findAllByMode(Mode mode);
}

