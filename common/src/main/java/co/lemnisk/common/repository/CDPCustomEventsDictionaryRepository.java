package co.lemnisk.common.repository;

import co.lemnisk.common.model.CDPCustomEventsDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CDPCustomEventsDictionaryRepository extends JpaRepository<CDPCustomEventsDictionary, Integer> {
    List<CDPCustomEventsDictionary> findByIsActive(int active);
}
