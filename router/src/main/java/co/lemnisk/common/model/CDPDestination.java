package co.lemnisk.common.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPDestination")
@Cacheable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPDestination {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Liveness")
    private Integer liveness;

}
