package com.safe.campus.model.vo;

import lombok.Data;

@Data
public class ListStudentByTeacherVo {
    private Long sId;
    private String sName;
    private String sNumber;
    private Integer type;
    private Integer state;
}
