package com.entity.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    private int id;
    private String currencyId;
    private String name;
    private String code;
    private String code2;
}
