package com.safe.campus.model.dto;

import lombok.Data;

@Data
public class STDto {

    private SInfo sInfo;
    private TInfo tInfo;


    @Data
    public static class SInfo {
        private Long sId;
        private String sNumber;
        private String className;
        private String classInfoName;
        private Integer type;
        private String buildingNo;
        private String buildingLevel;
        private String buildingRoom;
        private String buildingBed;
    }

    @Data
    public static class TInfo {
        private Long tId;
        private String tNumber;
        private String sectionName;
    }
}
