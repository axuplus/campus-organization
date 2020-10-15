package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingRoom;
import com.safe.campus.model.dto.BuildingRoomDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 楼层房间表 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@Component
@Mapper
public interface BuildingRoomMapper extends BaseMapper<BuildingRoom> {

    @Select("SELECT\n" +
            "\tr.id AS room_id,\n" +
            "\tr.building_room AS room_name,\n" +
            "\tl.building_level AS level_name,\n" +
            "\tn.building_no AS no_name \n" +
            "FROM\n" +
            "\tbuilding_room r\n" +
            "\tLEFT JOIN building_level l ON r.building_level_id = l.id\n" +
            "\tLEFT JOIN building_no n ON l.building_no_id = n.id \n" +
            "WHERE\n" +
            "\tn.master_id = #{masterId}")
    List<BuildingRoomDto> selectByNothing(@Param("masterId") Long masterId);

    @Select("SELECT\n" +
            "\tr.id AS room_id,\n" +
            "\tr.building_room AS room_name,\n" +
            "\tl.building_level AS level_name,\n" +
            "\tn.building_no AS no_name \n" +
            "FROM\n" +
            "\tbuilding_room r\n" +
            "\tLEFT JOIN building_level l ON r.building_level_id = l.id\n" +
            "\tLEFT JOIN building_no n ON l.building_no_id = n.id \n" +
            "WHERE\n" +
            "\tn.id =  #{id}")
    List<BuildingRoomDto> selectByNoId(@Param("id") Long id);

    @Select("SELECT\n" +
            "\tr.id AS room_id,\n" +
            "\tr.building_room AS room_name,\n" +
            "\tl.building_level AS level_name,\n" +
            "\tn.building_no AS no_name \n" +
            "FROM\n" +
            "\tbuilding_room r\n" +
            "\tLEFT JOIN building_level l ON r.building_level_id = l.id\n" +
            "\tLEFT JOIN building_no n ON l.building_no_id = n.id \n" +
            "WHERE\n" +
            "\tl.id =  #{id}")
    List<BuildingRoomDto> selectByLevelId(@Param("id") Long id);

    @Select("SELECT\n" +
            "\tr.id AS room_id,\n" +
            "\tr.building_room AS room_name,\n" +
            "\tl.building_level AS level_name,\n" +
            "\tn.building_no AS no_name \n" +
            "FROM\n" +
            "\tbuilding_room r\n" +
            "\tLEFT JOIN building_level l ON r.building_level_id = l.id\n" +
            "\tLEFT JOIN building_no n ON l.building_no_id = n.id \n" +
            "WHERE\n" +
            "\tr.id = #{id}")
    List<BuildingRoomDto> selectByRoomId(@Param("id") Long id);
}
