package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WhiteListVo {


    @ApiModelProperty(value = "0学生信息 1教师信息")
    private Integer state;
    private List<TeacherInfos> teacherInfos;
    private List<StudentInfos> studentInfos;


    @Data
    public static class StudentInfos {
        @ApiModelProperty(value = "学生ID")
        private Long studentId;
        @ApiModelProperty(value = "学生姓名")
        private String studentName;
        @ApiModelProperty(value = "学生性别")
        private Integer sex;
        @ApiModelProperty(value = "身份证号码")
        private String idNumber;
        @ApiModelProperty("1住校生 2通校生")
        private Integer type;
        @ApiModelProperty(value = "入学年份")
        private String joinTime;
        @ApiModelProperty("毕业年份")
        private String endTime;
        private String img;
        @ApiModelProperty(value = "年级ID")
        private Long classId;
        @ApiModelProperty(value = "年级名称")
        private String className;
        @ApiModelProperty(value = "班级ID")
        private Long classInfoId;
        @ApiModelProperty(value = "班级名称")
        private String classInfoName;
    }

    @Data
    public static class TeacherInfos {
        @ApiModelProperty("id")
        private Long id;
        @ApiModelProperty("姓名")
        private String name;
        @ApiModelProperty("工号")
        private String tNumber;
        @ApiModelProperty("性别 1:男 0：女")
        private Integer sex;
        @ApiModelProperty("身份证号码")
        private String idNumber;
        private String img;
        @ApiModelProperty("电话")
        private Long phone;
        @ApiModelProperty("部门名称")
        private String sectionName;
        @ApiModelProperty("部门ID")
        private Long sectionId;
        @ApiModelProperty("职务")
        private String position;
    }
}
