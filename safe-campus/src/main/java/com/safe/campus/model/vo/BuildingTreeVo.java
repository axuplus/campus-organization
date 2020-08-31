package com.safe.campus.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BuildingTreeVo {


    @ApiModelProperty(value = "楼幢ID")
    private Long buildingNoId;
    @ApiModelProperty(value = "楼幢名称")
    private String buildingNoName;
    private List<BuildingLevel> buildingLevel;

    @Data
    public static class BuildingLevel {
        @ApiModelProperty(value = "楼层ID")
        private Long buildingLevelId;
        @ApiModelProperty("楼层名称")
        private Integer buildingLevelName;
        private List<BuildingRoom> buildingRoom;

        @Data
        public static class BuildingRoom {
            @ApiModelProperty("宿舍ID")
            private Long buildingRoomId;
            @ApiModelProperty("宿舍编号")
            private Integer buildingRoomName;
        }
    }

}
