package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    private int id;
    private String name, code;
    private Date updatedAt;
    private Date createdAt;
    private String createdBy;
    private String status;
    private Date endDate;
    private Date startDate;
    private List<AssociatedApp> associatedApps;
    private Address address;
}
