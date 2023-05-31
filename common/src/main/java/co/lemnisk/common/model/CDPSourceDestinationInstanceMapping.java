package co.lemnisk.common.model;

import co.lemnisk.common.constants.Status;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPSourceDestinationInstanceMapping")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPSourceDestinationInstanceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CDPSourceInstanceId")
    private Integer cdpSourceInstanceId;

    @Column(name = "CDPDestinationInstanceId")
    private Integer cdpDestinationInstanceId;

    @Enumerated(EnumType.STRING)
    private Status status;
}
