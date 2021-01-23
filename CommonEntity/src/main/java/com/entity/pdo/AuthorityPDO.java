package com.entity.pdo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="authority")
public class AuthorityPDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "authority_id")
    private String authorityId;

    @Column(name = "jurisdiction_code")
    private String jurisdictionCode;

        @Column(name = "domains")
    private String domains;

    @Column(name = "departments")
    private String departments;

}
