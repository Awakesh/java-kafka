package co.lemnisk.common.model;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPEventsDictionary")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPEventsDictionary {

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

    @Column(name = "EventSchema")
    private String eventSchema;

    @Column(name = "AllowedDestList")
    private String allowedDestList;

}
