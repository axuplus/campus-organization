package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface BuildingStudentMapper extends BaseMapper<BuildingStudent> {


    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\t`building_student` \n" +
            "WHERE\n" +
            "\tno_id IN (\n" +
            "\tSELECT\n" +
            "\t\tid \n" +
            "\tFROM\n" +
            "\t\tbuilding_no \n" +
            "\tWHERE\n" +
            "\tmaster_id = #{masterId}) order by create_time desc")
    List<BuildingStudent> getThisSchoolStudent(@Param("masterId") Long masterId);
}
