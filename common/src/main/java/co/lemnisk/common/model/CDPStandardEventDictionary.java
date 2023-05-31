package co.lemnisk.common.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "CDPStandardEventDictionary")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPStandardEventDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "EventName")
    private String eventName;

    @Column(name = "Type")
    private String eventType;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="CDPSED_Id", referencedColumnName="Id")
    private Set<CDPStandardEventsPropsCampaignMapping> cdpStandardEventsPropsCampaignMappings;

}
