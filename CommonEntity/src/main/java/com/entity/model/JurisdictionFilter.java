package com.entity.model;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JurisdictionFilter {

    String column;
    List<String> values;
    String operator;

}

