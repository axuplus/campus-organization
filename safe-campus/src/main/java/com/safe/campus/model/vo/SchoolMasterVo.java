package com.safe.campus.model.vo;

import com.safe.campus.model.dto.SchoolMasterDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SchoolMasterVo {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("上级节点ID")
    private Long rootId;
    @ApiModelProperty("admin ID")
    private Long adminId;
    @ApiModelProperty("学校名称")
    private String schoolName;
    @ApiModelProperty("账号")
    private String account;
    private SchoolMasterDto.ServiceTime serviceTime;
    @ApiModelProperty("具体地址")
    private String address;
    @ApiModelProperty("logo图片")
    private String logo;
    @ApiModelProperty("学校实景图片")
    private String realPicture;
    @ApiModelProperty("状态 1：禁用 0：启用")
    private Integer state;
    private String appKey;
    private String appSecret;

    @Data
    public static class ServiceTime {
        @ApiModelProperty("起始时间")
        private Date startTime;
        @ApiModelProperty("结束时间")
        private Date endTime;
    }
}
