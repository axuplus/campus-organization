package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolTeacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 教职工信息 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-03
 */
@Component
@Mapper
public interface SchoolTeacherMapper extends BaseMapper<SchoolTeacher> {

    @Update("update school_teacher set section_id = null where section_id = #{sectionId}")
    void updateBySectionId(@Param("sectionId") Long sectionId);

    @Update("update school_teacher set section_id = #{sectionId} where id = #{tId}")
    void updateNewSectionId(@Param("sectionId") Long sectionId, @Param("tId") Long tId);
}
