package com.safe.campus.model.vo;

import com.safe.campus.model.domain.BuildingBed;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BuildingTreeVo {


    @ApiModelProperty(value = "楼幢ID")
    private Long buildingNoId;
    @ApiModelProperty(value = "1 类型")
    private Integer type;
    @ApiModelProperty(value = "楼幢名称")
    private String buildingNoName;
    private List<BuildingLevel> buildingLevels;

    @Data
    public static class BuildingLevel {
        @ApiModelProperty(value = "楼层ID")
        private Long buildingLevelId;
        @ApiModelProperty(value = "2 类型")
        private Integer type;
        @ApiModelProperty("楼层名称")
        private String buildingLevelName;
        private List<BuildingRoom> buildingRooms;

        @Data
        public static class BuildingRoom {
            @ApiModelProperty("宿舍ID")
            private Long buildingRoomId;
            @ApiModelProperty(value = "3 类型")
            private Integer type;
            @ApiModelProperty("宿舍编号")
            private String buildingRoomName;
            private List<BuildingBed> buildingBeds;

            @Data
            public static class BuildingBed{
                @ApiModelProperty(value = "床位id")
                private Long bedId;
                @ApiModelProperty(value = "4 类型")
                private Integer type;
                @ApiModelProperty(value = "床位")
                private String bedName;
            }
        }
    }

}
