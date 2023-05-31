package co.lemnisk.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Campaign {
    @Id
    @Column
    private Integer id;
}
