package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class STDto {

    private Long schoolId;
    private SInfo sInfo;
    private TInfo tInfo;


    @Data
    public static class SInfo {
        private Long sId;
        private String sNumber;
        @ApiModelProperty(value = "年级名称")
        private String className;
        @ApiModelProperty(value = "班级名称")
        private String classInfoName;
        private String sIdNumber;
        @ApiModelProperty(value = "1：住校生 2：同校生")
        private Integer type;
        @ApiModelProperty(value = "楼幢")
        private String buildingNo;
        @ApiModelProperty(value = "楼层")
        private String buildingLevel;
        @ApiModelProperty(value = "宿舍")
        private String buildingRoom;
        @ApiModelProperty(value = "床位")
        private String buildingBed;
    }

    @Data
    public static class TInfo {
        private Long tId;
        @ApiModelProperty(value = "职务")
        private String position;
        private Long phone;
        private String idNumber;
        private String tNumber;
        @ApiModelProperty(value = "部门")
        private String sectionName;
    }
}
