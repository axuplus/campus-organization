package com.data.model.dto;

import lombok.Data;

@Data
public class Records {
    // id
    private String StaffId;
    // 编号
    private String StaffNo;
    // 姓名
    private String StaffName;
    // 设备类型
    private String DevTypeCode;
    // 设备编号
    private String DevNo;
    // 设备名称
    private String DevName;
    // 门牌号
    private String DoorNo;
    // 开门时间
    private String OpenDate;
    // 照片id
    private String OpenPicNo;
    // 项目id
    private String Gid;

}
