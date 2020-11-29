package com.smoke.service.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class DeviceExcelDto extends BaseRowModel {
    @ExcelProperty(value = "设备ID", index = 0)
    @ApiModelProperty(value = "设备ID",required = true)
    private String deviceId;
    @ExcelProperty(value = "设备名称", index = 1)
    @ApiModelProperty(value = "设备名称",required = true)
    private String deviceName;
    @ExcelProperty(value = "设备厂家", index = 2)
    @ApiModelProperty(value = "设备厂家",required = true)
    private String deviceFactory;
    @ExcelProperty(value = "设备型号", index = 3)
    @ApiModelProperty(value = "设备型号",required = true)
    private String deviceModel;
    @ExcelProperty(value = "安装位置", index = 4)
    @ApiModelProperty(value = "安装位置",required = true)
    private String deviceLocation;
}
