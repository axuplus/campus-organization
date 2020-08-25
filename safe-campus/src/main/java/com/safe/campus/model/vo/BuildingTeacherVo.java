package com.safe.campus.model.vo;
import lombok.Data;
import java.util.Map;

@Data
public class BuildingTeacherVo {
    private Long buildingId;
    private String buildingNo;
    private Map levels;
}
