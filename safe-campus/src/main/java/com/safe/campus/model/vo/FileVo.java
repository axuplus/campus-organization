package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "FileVo", description = "文件信息")
@Data
public class FileVo implements java.io.Serializable {

    @ApiModelProperty(value = "文件URL", name = "fileUrl")
    private String fileUrl;

    @ApiModelProperty(value = "缩略图URL", name = "thumbUrl")
    private String thumbUrl;

}
