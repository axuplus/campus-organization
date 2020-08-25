package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BuildingStudentMapper extends BaseMapper<BuildingStudent> {

    @Select("select * from building_student where bed_no =#{bedNo} and room_id = " +
            "(select id from building_room where building_room = #{roomName} and building_level_id = " +
            "(select id from building_level where building_level = #{levelName} and building_no_id = " +
            "(select id from building_no where building_no ={noName})))")
    BuildingStudent checkBedIsFull(@Param("bedNo") String bedNo, @Param("roomName") String roomName, @Param("levelName") String levelName, @Param("noName") String noName);
}
