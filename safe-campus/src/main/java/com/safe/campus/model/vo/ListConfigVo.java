package com.safe.campus.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class ListConfigVo {
    private String schoolName;
    private List<Modules> modules;

    @Data
    public static class Modules {
        private String moduleName;
        private List<SubInfo> subInfo;

        @Data
        public static class SubInfo {
            private Long id;
            private Integer state;
            private String subName;
        }
    }
}
