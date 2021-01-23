package com.entity.pdo;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "associated_app")
public class AssociatedAppPDO {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "percentage")
    private Float percentage;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "entity_id", referencedColumnName = "id")
    private EntityPDO entityPDO;
}
