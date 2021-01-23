package com.entity.pdo;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "definition")
public class DefinitionPDO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "field_count")
    private int fieldCount;

    @Column(name = "record_count")
    private int recordCount;

    @OneToMany(mappedBy = "definitionPDO",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<FieldPDO> fieldPDOS;

}
