package com.safe.campus.about.utils.wrapper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseQueryDto {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "页码", name = "pageNum")
    private Integer pageNum = 1




            ;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "条数", name = "pageSize")
    private Integer pageSize = 3;


}
