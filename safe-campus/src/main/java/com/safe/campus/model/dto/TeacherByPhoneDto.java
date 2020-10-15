package com.safe.campus.model.dto;

import lombok.Data;

@Data
public class TeacherByPhoneDto {
    private Long masterId;
    private String masterName;
    private Long teacherId;
    private Long sectionId;
    private String sectionName;
    private String img;
}
