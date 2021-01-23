package com.entity.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociatedApp {

    private int id;
    private String name;
    private float percentage;
    private String status;
    private int entityId;

}

