package com.safe.campus.model.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
@TableName(value = "sys_file")
public class SysFileDomain {

    @Id
    private Long id;

    private String fileName;

    private String fileSuffix;

    private Long fileSize;

    private Integer fileType;

    private String filePath;

    private String fileUrl;

    private String thumbName;

    private String thumbPath;

    private String thumbUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
