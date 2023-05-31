package co.lemnisk.consumer.repo;

import co.lemnisk.consumer.entity.CDPDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CDPDestinationRepo extends JpaRepository<CDPDestination, Long> {
}
