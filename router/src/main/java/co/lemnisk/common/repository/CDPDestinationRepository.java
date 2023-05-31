package co.lemnisk.common.repository;


import co.lemnisk.common.model.CDPDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDPDestinationRepository extends JpaRepository<CDPDestination, Integer> {
    CDPDestination getById(Integer id);
}

