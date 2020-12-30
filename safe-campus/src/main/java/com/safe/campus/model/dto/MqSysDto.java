package com.safe.campus.model.dto;

import lombok.Data;

@Data
public class MqSysDto {
    private Long userId;
    private Long masterId;
    private String name;
    private Integer type;
    private String idNumber;
    private Long classId;
    private Long classInfoId;
}
