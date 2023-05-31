package co.lemnisk.consumer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "CDPDestination")
public class CDPDestination implements Serializable {
    @Id
    @Column(name="Id")
    private Long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "JsonSchema")
    private String jsonSchema;
    @Column(name = "Path")
    private String path;
    @Column(name = "AvailableSource")
    private String availableSource;
    @Column(name = "UiConfig")
    private String uiConfig;
    @Column(name = "Liveness")
    private int liveness;

  /*  @OneToOne(fetch = FetchType.EAGER,
            //cascade =  CascadeType.ALL,
            mappedBy = "cdpDestination")
    private CDPDestinationInstance cdpDestinationInstance;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

