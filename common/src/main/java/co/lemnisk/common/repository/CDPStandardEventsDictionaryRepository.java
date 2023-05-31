package co.lemnisk.common.repository;

import co.lemnisk.common.model.CDPStandardEventDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CDPStandardEventsDictionaryRepository extends JpaRepository<CDPStandardEventDictionary, Integer> {
}
