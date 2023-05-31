package co.lemnisk.common.repository;

import co.lemnisk.common.constants.Mode;
import co.lemnisk.common.model.CDPPassThroughTransformers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPPassThroughTransformersRepository extends JpaRepository<CDPPassThroughTransformers, Integer> {
    List<CDPPassThroughTransformers> findAllByMode(Mode mode);
}
