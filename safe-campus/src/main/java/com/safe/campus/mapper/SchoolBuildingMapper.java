package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Mapper
public interface SchoolBuildingMapper extends BaseMapper<SchoolBuildingMapper> {


    @Select("select id,class_name from school_building where building_name = #{buildingName}")
    Map  getBuildingInfo(@Param("buildingName") String str);
}
