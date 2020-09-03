package com.safe.campus.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class NodeTreeVo {
    private Long classId;
    private Integer type;
    private String className;
    private List<SubClass> subClassInfo;

    @Data
    public static class SubClass{
        private Long subClassId;
        private Integer type;
        private String subClassName;
    }
}
