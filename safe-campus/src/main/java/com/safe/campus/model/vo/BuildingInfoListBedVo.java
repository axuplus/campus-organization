package com.safe.campus.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BuildingInfoListBedVo {
    @ApiModelProperty(value = "roomId")
    private Long roomId;
    @ApiModelProperty(value = "当前宿舍信息")
    private String currentBuildingInfo;

    private List<Bed> beds;

    @Data
    public static class Bed {
        @ApiModelProperty(value = "床位")
        private String bedName;
        @ApiModelProperty(value = "学生姓名")
        private String sName;
        @ApiModelProperty(value = "学号")
        private String sNumber;
        @ApiModelProperty(value = "班级年级信息")
        private String classInfo;
        @ApiModelProperty(value = "班主任信息")
        private String teacherInfo;
        @ApiModelProperty(value = "状态 1在校 0不在")
        private Integer state;
        @ApiModelProperty(value = "更新时间")
        private String time;
        @ApiModelProperty(value = "数据设备")
        private String equipmentName;
    }
}
