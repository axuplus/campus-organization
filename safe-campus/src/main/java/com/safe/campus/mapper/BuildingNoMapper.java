package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingNo;
import com.safe.campus.model.dto.BuildingManagerDto;
import com.safe.campus.model.dto.BuildingNoMapperDto;
import com.safe.campus.model.dto.BuildingStudentListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 楼幢表 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@Component
@Mapper
public interface BuildingNoMapper extends BaseMapper<BuildingNo> {


    @Select("SELECT\n" +
            "\ta.id AS noId,\n" +
            "\tb.id AS levelId,\n" +
            "\tc.id AS roomId,\n" +
            "\td.id AS bed_id \n" +
            "FROM\n" +
            "\tbuilding_no a\n" +
            "\tINNER JOIN building_level b ON a.id = b.building_no_id\n" +
            "\tINNER JOIN building_room c ON b.id = c.building_level_id\n" +
            "\tINNER JOIN building_bed d ON c.id = d.room_id \n" +
            "WHERE\n" +
            "\ta.master_id = #{masterId} \n" +
            "\tAND a.building_no = #{buildingNo}\n" +
            "\tAND b.building_level = #{buildingLevel}\n" +
            "\tAND c.building_room = #{buildingRoom}\n" +
            "\tAND d.bed_name = #{buildingBed}")
    BuildingNoMapperDto checkBuildingInfo(Long masterId, String buildingNo, String buildingLevel, String buildingRoom, String buildingBed);


    @Select("SELECT\n" +
            "\ta.building_no,\n" +
            "\tb.building_level,\n" +
            "\tc.building_room,\n" +
            "\td.bed_name \n" +
            "FROM\n" +
            "\tbuilding_no a,\n" +
            "\tbuilding_level b,\n" +
            "\tbuilding_room c,\n" +
            "\tbuilding_bed d \n" +
            "WHERE\n" +
            "\ta.id = #{aId}\n" +
            "\tAND b.id = #{bId}\n" +
            "\tAND c.id = #{cId}\n" +
            "\tAND d.id = #{dId}")
    BuildingStudentListDto checkBuildingInfoByIds(@Param("aId") Long aId, @Param("bId") Long bId, @Param("cId") Long cId, @Param("dId") Long dId);


    @Select("" +
            "SELECT\n" +
            "\ta.id as noId,\n" +
            "\ta.building_no,\n" +
            "\tb.id as levelId,\n" +
            "\tb.t_id,\n" +
            "\tb.building_level \n" +
            "FROM\n" +
            "\tbuilding_no a\n" +
            "\tINNER JOIN building_level b ON a.id = b.building_no_id \n" +
            "WHERE\n" +
            "\ta.master_id = #{masterId}")
    List<BuildingManagerDto> selectNoByMasterId(@Param("masterId") Long masterId);

}
