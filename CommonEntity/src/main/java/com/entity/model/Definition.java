package com.entity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Definition {
    private int id;
    private String name;
    private int fieldCount;
    private int recordCount;
    private List<Field> fields;

}
