package com.entity.pdo;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "domain")
public class DomainPDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "code")
    private String code;

    @Column(name = "alternate_name")
    private String alternateName;

    @Column(name = "associated_apps")
    private String associatedApps;

}
