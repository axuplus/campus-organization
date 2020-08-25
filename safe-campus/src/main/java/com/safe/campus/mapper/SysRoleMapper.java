package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Component
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    @Select("select id from sys_role where role_name = #{roleName}")
    Long checkIdByRoleName(String role);
}
