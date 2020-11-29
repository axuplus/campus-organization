package com.smoke.service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataTran {
    private String title;
    private String id;
    private List<DataStream> datastreams;


    @Data
    public static class DataStream {
        private String at;
        private String id;
        private List<String> value;
    }
}
