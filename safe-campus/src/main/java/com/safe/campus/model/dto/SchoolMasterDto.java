package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SchoolMasterDto {

    @ApiModelProperty(value = "新增不用管,编辑的话传给我", required = false)
    private Long id;
    @ApiModelProperty(value = "rootId", required = true)
    private Long rootId;
    @ApiModelProperty(value = "学校名称", required = true)
    private String schoolName;
    @ApiModelProperty(value = "账号", required = true)
    private String account;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    private ServiceTime serviceTime;
    private CityInfo cityInfo;
    @ApiModelProperty(value = "具体地址", required = true)
    private String address;
    @ApiModelProperty(value = "logo图片", required = true)
    private String logo;
    @ApiModelProperty(value = "学校实景图片", required = true)
    private String realPicture;
    @ApiModelProperty(value = "状态 1：禁用 0：启用", required = true)
    private Integer state;


    @Data
    public static class ServiceTime {
        @ApiModelProperty(value = "起始时间", required = true)
        private String startTime;
        @ApiModelProperty(value = "结束时间", required = true)
        private String endTime;
    }

    @Data
    public static class CityInfo {
        @ApiModelProperty(value = "省份", required = true)
        private String province;
        @ApiModelProperty(value = "城市", required = true)
        private String city;
        @ApiModelProperty(value = "区", required = true)
        private String areas;
    }
}
