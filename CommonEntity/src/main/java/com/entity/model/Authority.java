package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    private int id;
    private String authorityId;
    private String name;
    private String jurisdictionType;
    private String jurisdictionLocation;
    private String jurisdictionName;
    private String jurisdictionCountry;
    private String jurisdictionCode;
    private String domainName;
    private String departments;
}
