package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-05
 */
@Component
@Mapper
public interface SchoolStudentMapper extends BaseMapper<SchoolStudent> {

    @Select("select * from school_student where class_info_id = #{classInfoId} ORDER BY  convert(s_name  using gbk)  asc")
    List<SchoolStudent> selectStudentsOrderByName(@Param("classInfoId")Long classInfoId);
}
