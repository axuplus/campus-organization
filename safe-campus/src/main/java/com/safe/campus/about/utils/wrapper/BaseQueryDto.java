package com.safe.campus.about.utils.wrapper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseQueryDto {
    /**
     * 当前页
     */
    @ApiModelProperty(value = "页码", name = "page")
    private Integer page = 1




            ;

    /**
     * 每页条数 别问我为什么下划线 前端让改的
     */
    @ApiModelProperty(value = "条数", name = "page_size")
    private Integer page_size = 5;


}
