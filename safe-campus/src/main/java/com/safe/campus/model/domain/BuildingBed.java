package com.safe.campus.model.domain;

import lombok.Data;

@Data
public class BuildingBed {
    private Long id;
    private Long roomId;
    private String bedName;
}
