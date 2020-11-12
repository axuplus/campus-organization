package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

/**
 * <p>
 * 用户角色 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Component
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @Select("SELECT\n" +
            "\ta.role_name \n" +
            "FROM\n" +
            "\tsys_role a\n" +
            "\tLEFT JOIN sys_user_role b ON a.id = b.role_id \n" +
            "WHERE\n" +
            "\tb.user_id = #{userId}")
    List<String> getRoleNamesByUserId(@Param("userId") Long userId);
}
