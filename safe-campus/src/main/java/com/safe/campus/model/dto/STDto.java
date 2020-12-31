package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class STDto {

    @ApiModelProperty(value = "学校ID")
    private Long schoolId;
    private SInfo sInfo;
    private TInfo tInfo;


    @Data
    public static class SInfo {
        @ApiModelProperty(value = "学生ID")
        private Long sId;
        @ApiModelProperty(value = "学号")
        private String sNumber;
        @ApiModelProperty(value = "年级名称")
        private String className;
        @ApiModelProperty(value = "班级名称")
        private String classInfoName;
        @ApiModelProperty(value = "学生身份证号码")
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
        @ApiModelProperty(value = "教师ID")
        private Long tId;
        @ApiModelProperty(value = "职务")
        private String position;
//        @ApiModelProperty(value = "教师角色")
//        private String teacherRole;
        @ApiModelProperty(value = "教师电话")
        private Long phone;
        @ApiModelProperty(value = "教师身份证")
        private String idNumber;
        @ApiModelProperty(value = "教职工编号")
        private String tNumber;
        @ApiModelProperty(value = "部门")
        private String sectionName;
    }
}
