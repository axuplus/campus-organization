package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolClassInfo;
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
public interface SchoolClassInfoMapper extends BaseMapper<SchoolClassInfo> {
    @Select("select id,class_name from school_class_info where class_name = #{className}")
    Map getClassInfo(@Param("className") String str);
}
