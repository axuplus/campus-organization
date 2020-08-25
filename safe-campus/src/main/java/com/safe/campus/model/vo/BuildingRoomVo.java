package com.safe.campus.model.vo;

import lombok.Data;

@Data
public class BuildingRoomVo {

    private Long id;
    private Long studentId;
    private String sName;
    private String sex;
    private String sNumber;
    private String buildingNo;
    private Integer buildingLevel;
    private Integer buildingRoom;
    private Integer bedNo;
}
