package com.safe.campus.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class StudentExcelDto extends BaseRowModel {
    @ExcelProperty(value = "姓名", index = 0)
    private String name;
    @ExcelProperty(value = "学号", index = 1)
    private String sNumber;
    @ExcelProperty(value = "性别", index = 2)
    private String sex;
    @ExcelProperty(value = "身份证号", index = 3)
    private String idNumber;
    @ExcelProperty(value = "学生类型", index = 4)
    private String type;
    @ExcelProperty(value = "学生年级", index = 5)
    private String classLevel;
    @ExcelProperty(value = "学生班级", index = 6)
    private String classInfo;
    @ExcelProperty(value = "楼幢", index = 7)
    private String buildingNo;
    @ExcelProperty(value = "楼层", index = 8)
    private String buildingLevel;
    @ExcelProperty(value = "宿舍", index = 9)
    private String buildingRoom;
    @ExcelProperty(value = "床位", index = 10)
    private String buildingBed;
    @ExcelProperty(value = "入学年月", index = 11)
    private String start;
    @ExcelProperty(value = "毕业年月", index = 12)
    private String end;
}
