package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TeacherInfoDto {
    @ApiModelProperty(value = "编辑传这个ID 新增不用管", required = false)
    private Long id;
    @ApiModelProperty(value = "学校ID", required = true)
    private Long masterId;
    @ApiModelProperty(value = "姓名", required = true)
    private String name;
    @ApiModelProperty(value = "工号", required = true)
    private String tNumber;
    @ApiModelProperty(value = "1：男 0：女", required = true)
    private Integer sex;
    @ApiModelProperty(value = "身份证号码", required = true)
    private String idNumber;
    @ApiModelProperty(value = "加入时间", required = true)
    private String joinTime;
    @ApiModelProperty(value = "部门ID", required = true)
    private Long sectionId;
    @ApiModelProperty(value = "职务", required = true)
    private String position;
    @ApiModelProperty(value = "图片ID", required = true)
    private Long imgId;
    @ApiModelProperty(value = "电话", required = true)
    private Long phone;
}
