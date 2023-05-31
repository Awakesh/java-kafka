package co.lemnisk.common.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "CDPSourceInstance")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CDPSourceInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "CDPSourceId")
    private Integer cdpSourceId;

    @Column(name = "Name")
    private String name;

    @Column(name = "ConfigJson")
    private String configJson;

    @Column(name = "WriteKey")
    private String writeKey;

    @Column(name = "CreatedOn")
    private Date createdOn;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "UpdatedOn")
    private Date updatedOn;

    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    @Column(name = "Status")
    private String status;

    @Column(name = "CampaignId")
    private Integer campaignId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CDPSourceInstance that = (CDPSourceInstance) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}