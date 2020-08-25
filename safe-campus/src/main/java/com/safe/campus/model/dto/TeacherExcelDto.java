package com.safe.campus.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class TeacherExcelDto extends BaseRowModel {

    @ExcelProperty(value = "姓名",index = 0)
    private String name;
    @ExcelProperty(value = "工号",index = 1)
    private String tNumber;
    @ExcelProperty(value = "性别",index = 2)
    private String sex;
    @ExcelProperty(value = "身份证号",index = 3)
    private String idNumber;
    @ExcelProperty(value = "手机号",index = 4)
    private String phone;
    @ExcelProperty(value = "入职年份",index = 5)
    private String joinTime;
    @ExcelProperty(value = "部门",index = 6)
    private String sectionName;
    @ExcelProperty(value = "职务",index = 7)
    private String positionName;
    @ExcelProperty(value = "角色",index = 8)
    private String roleName;
    @ExcelProperty(value = "状态",index = 9)
    private String shape;
}
