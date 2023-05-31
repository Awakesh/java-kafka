package co.lemnisk.common.model;

import co.lemnisk.common.constants.Mode;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPStandardEventsTransformers")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPStandardEventsTransformers {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "DestinationId")
    private Integer destinationId;

    @Column(name = "EventName")
    private String eventName;

    @Column(name = "Function")
    private String function;

    @Column(name = "Mode")
    @Enumerated(EnumType.STRING)
    private Mode mode;
}
