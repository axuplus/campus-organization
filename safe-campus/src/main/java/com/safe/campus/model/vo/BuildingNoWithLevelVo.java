package com.safe.campus.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class BuildingNoWithLevelVo {
    private Long id;
    private String buildingNo;
    private List<BuildingLevels> buildingLevels;

    @Data
    public static class BuildingLevels {
        private Long id;
        private String buildingLevel;
    }
}
