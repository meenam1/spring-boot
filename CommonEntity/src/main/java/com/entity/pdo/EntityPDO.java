package com.entity.pdo;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@TypeDef(name="JsonNodeType", typeClass = JsonNodeType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Entity(name = "entity")
public class EntityPDO {


    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Type(type = "JsonNodeType")
    @Column(name = "data")
    private JsonNode data;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "entityPDO",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<AssociatedAppPDO> associatedAppPDOS;

}
