package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Joma
 * @since 2020-07-30
 */
@Mapper
@Component
public interface SchoolClassMapper extends BaseMapper<SchoolClass> {

    @Select("select id,class_name from school_class where class_name = #{className}")
    Map getClassInfo(@Param("className") String str);
}
