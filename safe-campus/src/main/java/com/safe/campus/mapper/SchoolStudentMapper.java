package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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

    int saveOrUpdate(SchoolStudent schoolStudent);

}
