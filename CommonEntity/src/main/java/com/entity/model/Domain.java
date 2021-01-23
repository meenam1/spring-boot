package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Domain {

    private int id;
    private String name;
    private String type;
    private String code;
    private String alternateName;
    private String associatedApps;

}
