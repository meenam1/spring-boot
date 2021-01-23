package com.adjustment.pdo;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
    @JoinColumn(name = "adjustment_id", referencedColumnName = "id")
    private AdjustmentPDO adjustmentPDO;
}
