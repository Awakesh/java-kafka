package co.lemnisk.common.model;

import co.lemnisk.common.constants.Status;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@Data
@Entity
@Table(name = "CDPDestinationInstance")
@Cacheable
@Transactional(propagation = Propagation.REQUIRES_NEW)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPDestinationInstance {

    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CampaignId")
    private Integer campaignId;

    @Column(name = "ConfigJson")
    private String configJson;

    @Column(name = "CDPDestinationId")
    private Integer cdpDestinationId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "UiConfig")
    private String mode;
}
