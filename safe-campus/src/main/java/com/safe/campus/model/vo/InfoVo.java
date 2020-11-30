package com.safe.campus.model.vo;

import lombok.Data;

@Data
public class InfoVo {


    @Data
    public static class Teacher {
        private Long id;
        private String teacherName;
        private String teacherNumber;
        private String idNumber;
        private String phone;
        private Integer gender;
    }


    @Data
    public static class Student {
        private Long id;
        private String studentName;
        private String studentNumber;
        private String idNumber;
        private String gradeName;
        private String className;
        private Integer gender;
    }
}
