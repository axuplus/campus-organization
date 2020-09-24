package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FaceImgInfoVo {

    @ApiModelProperty(value = "1学生信息 2老师信息")
    private Integer state;
    private StudentInfo studentInfo;
    private TeacherInfo teacherInfo;

    @Data
    public static class StudentInfo {
        private Long id;
        private String studentName;
        private String img;
        private String className;
        private String classInfoName;
        private Integer type;
    }

    @Data
    public static class TeacherInfo {
        private Long id;
        private String teacherName;
        private String tNumber;
        private String sectionName;

    }
}
