package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysAdmin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SysAdminUserMapper extends BaseMapper<SysAdmin> {

    @Select("SELECT\n" +
            "\tid \n" +
            "FROM\n" +
            "\t(\n" +
            "\tSELECT\n" +
            "\t\tt1.id,\n" +
            "\tIF\n" +
            "\t\t ( find_in_set( p_id, @pids ) > 0, @pids := concat( @pids, ',', id ), 0 ) AS ischild \n" +
            "\tFROM\n" +
            "\t\t( SELECT id, p_id FROM sys_admin t ORDER BY p_id, id ) t1,\n" +
            "\t\t( SELECT @pids := #{pids} ) t2\n" +
            "\t\t) t3 \n" +
            "WHERE\n" +
            "\tischild != 0")
    List<Long> getAdminUsers(@Param("pids") Long pids);
}
