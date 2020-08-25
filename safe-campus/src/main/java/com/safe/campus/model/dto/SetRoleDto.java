package com.safe.campus.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SetRoleDto {

    @ApiModelProperty("教师ID")
    private Long teacherId;
    @ApiModelProperty("角色ID  数组")
    private List<Long> roleId;
}
