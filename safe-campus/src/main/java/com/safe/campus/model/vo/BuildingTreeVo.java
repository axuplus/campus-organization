package com.safe.campus.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class BuildingTreeVo {


        private Long buildingNoId;
        private String buildingNoName;
        private List<BuildingLevel> buildingLevel;

    @Data
    public static class BuildingLevel {
        private Long buildingLevelId;
        private Integer buildingLevelName;
        private List<BuildingRoom> buildingRoom;

        @Data
        public static class BuildingRoom {
            private Long buildingRoomId;
            private Integer buildingRoomName;
        }
    }

}
