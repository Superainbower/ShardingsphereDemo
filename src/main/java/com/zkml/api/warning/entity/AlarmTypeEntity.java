package com.zkml.api.warning.entity;

import lombok.Data;

@Data
public class AlarmTypeEntity {
    private Integer id;
    private Integer type;
    private String context;
    private String description;
}
