package co.lemnisk.consumer.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "CDPDestinationApiDetails")
public class CDPDestinationApiDetails implements Serializable {
    @Id
    @Column(name="Id")
    private Integer id;
    @Column(name = "CdpDestinationId")
    private Integer cdpDestinationId;
    @Column(name = "Endpoint")
    private String endpoint;
    @Column(name = "RequestType")
    private String requestType;
    @Column(name = "Header")
    private String header;
    @Column(name = "QueryParameter")
    private String queryParameter;




}


