package co.lemnisk.common.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPCustomEventsDictionary")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPCustomEventsDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CampaignId")
    private Integer campaignId;

    @Column(name = "EventName")
    private String eventName;

    @Column(name = "LemEventName")
    private String lemEventName;

    @Column(name = "EventType")
    private String eventType;

    @Column(name = "AllowedDestinationInstanceList")
    private String allowedDestinationInstanceList;

    @Column(name = "IsActive")
    private Integer isActive;

}
