package com.safe.campus.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class BuildingExcelDto extends BaseRowModel {
    @ExcelProperty(value = "楼幢", index = 0)
    private String buildingNo;
    @ExcelProperty(value = "楼层", index = 1)
    private String buildingLevel;
    @ExcelProperty(value = "宿舍号", index = 2)
    private String buildingRoom;
    @ExcelProperty(value = "床位", index = 3)
    private String buildingBed;
}
