package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingBed;
import com.safe.campus.model.dto.BuildingBedDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BuildingBedMapper extends BaseMapper<BuildingBed> {

    @Select("SELECT\n" +
            "\ta.id AS buildingNoId,\n" +
            "\ta.building_no,\n" +
            "\tb.id AS buildingLevelId,\n" +
            "\tb.building_level,\n" +
            "\tc.id AS buildingRoomId,\n" +
            "\tc.building_room,\n" +
            "\td.id AS bedId,\n" +
            "\td.bed_name \n" +
            "FROM\n" +
            "\tbuilding_no a\n" +
            "\tINNER JOIN building_level b\n" +
            "\tINNER JOIN building_room c\n" +
            "\tINNER JOIN building_bed d ON a.id = b.building_no_id \n" +
            "\tAND b.id = c.building_level_id \n" +
            "\tAND c.id = d.room_id \n" +
            "WHERE\n" +
            "\td.id = #{bedId}")
    BuildingBedDto getLivingInfo(@Param("bedId") Long bedId);
}
