package co.lemnisk.common.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPCustomEventsTransformers")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPCustomEventsTransformers {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CampaignId")
    private Integer campaignId;

    @Column(name = "EventName")
    private String eventName;

    @Column(name = "DestinationInstanceId")
    private Integer destinationInstanceId;

    @Column(name = "Function")
    private String function;

    @Column(name = "IsActive")
    private Integer isActive;
}
