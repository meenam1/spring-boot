package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jurisdiction {
    private int id;
    private String name;
    private String type;
    private String code;
    private String country;
    private String location;
    private String associatedApps;

}
