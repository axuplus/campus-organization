package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AllStudentsVo {
    @ApiModelProperty(value = "学校id")
    private Long masterId;
    @ApiModelProperty(value = "学生ID")
    private Long studentId;
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    @ApiModelProperty(value = "学生性别")
    private Integer sex;
    @ApiModelProperty(value = "学号")
    private String sNumber;
    @ApiModelProperty(value = "照片")
    private String img;
    @ApiModelProperty(value = "身份证号码")
    private String idNumber;
    @ApiModelProperty("1住校生 2通校生")
    private Integer type;
    @ApiModelProperty(value = "入学年份")
    private String joinTime;
    @ApiModelProperty("毕业年份")
    private String endTime;
    private BuildingInfo buildingInfo;
    private ClassInfo classInfo;


    @Data
    public static class BuildingInfo {
        @ApiModelProperty(value = "楼幢id")
        private Long noId;
        @ApiModelProperty(value = "楼幢")
        private String noName;
        @ApiModelProperty(value = "楼层id")
        private Long levelId;
        @ApiModelProperty(value = "楼层")
        private String levelName;
        @ApiModelProperty(value = "宿舍id")
        private Long roomId;
        @ApiModelProperty(value = "宿舍")
        private String roomName;
        @ApiModelProperty(value = "床位ID")
        private Long bedId;
        @ApiModelProperty(value = "床位")
        private String bedName;
    }

    @Data
    public static class ClassInfo {
        @ApiModelProperty(value = "班主任ID")
        private Long teacherId;
        @ApiModelProperty(value = "班主任姓名")
        private String teacherName;
        @ApiModelProperty(value = "班主任工号")
        private String teacherNumber;
        @ApiModelProperty(value = "年级ID")
        private Long classId;
        @ApiModelProperty(value = "年级名称")
        private String className;
        @ApiModelProperty(value = "班级ID")
        private Long classInfoId;
        @ApiModelProperty(value = "班级名称")
        private String classInfoName;
    }
}
