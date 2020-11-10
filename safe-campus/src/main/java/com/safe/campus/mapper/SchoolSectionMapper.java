package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolSection;
import com.safe.campus.model.vo.SectionTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Joma
 * @since 2020-07-28
 */
@Mapper
@Component
public interface SchoolSectionMapper extends BaseMapper<SchoolSection> {

    @Select("SELECT\n" +
            "\tid \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tt1.id,\n" +
            "\tIF\n" +
            "\t\t( find_in_set( p_id, @pids ) > 0, @pids := concat( @pids, ',', id ), 0 ) AS ischild \n" +
            "\tFROM\n" +
            "\t\t( SELECT id, p_id FROM school_section t ORDER BY p_id, id ) t1,\n" +
            "\t\t( SELECT @pids := #{pids} ) t2 \n" +
            "\t) t3 \n" +
            "WHERE\n" +
            "\tischild != 0")
    List<Long> getSubTrees(@Param("pids") Long pIds);

    @Select("select * from school_section where state = 0 and is_delete = 0 and master_id = #{masterId}")
    List<SectionTreeVo> findAll(@Param("masterId") Long masterId);

    @Select("SELECT * FROM `school_section` WHERE section_name = #{sectionName} and master_id = #{masterId}")
    SchoolSection getSectionByName(@Param("sectionName") String sectionName, @Param("masterId") Long masterId);

}
