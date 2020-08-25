package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolRoot;
import com.safe.campus.model.vo.SchoolRootTreeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-18
 */
@Component
@Mapper
public interface SchoolRootMapper extends BaseMapper<SchoolRoot> {


    @Select("SELECT\n" +
            "\tid \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tt1.id,\n" +
            "\tIF\n" +
            "\t\t( find_in_set( p_id, @pids ) > 0, @pids := concat( @pids, ',', id ), 0 ) AS ischild \n" +
            "\tFROM\n" +
            "\t\t( SELECT id, p_id FROM school_root t ORDER BY p_id, id ) t1,\n" +
            "\t\t( SELECT @pids := #{pids} ) t2 \n" +
            "\t) t3 \n" +
            "WHERE\n" +
            "\tischild != 0")
    List<Long> getSubTrees(@Param("pids") Long pIds);


    @Select("select * from school_root")
    List<SchoolRootTreeVo> findAll();

    @Select("select * from school_root where p_id = #{pId}")
    SchoolRoot checkSub(@Param("pId")Long pId);
}
