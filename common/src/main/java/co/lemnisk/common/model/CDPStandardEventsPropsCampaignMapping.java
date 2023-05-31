package co.lemnisk.common.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPStandardEventsPropsCampaignMapping")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPStandardEventsPropsCampaignMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CDPSED_Id")
    private Integer cdpStandardEventDictionaryId;

    @Column(name = "CampaignId")
    private Integer campaignId;

    @Column(name = "AllowedDestinationInstanceList")
    private String allowedDestinationInstanceList;

    @Column(name = "IsActive")
    private Integer isActive;
}
