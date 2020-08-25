package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

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

    @Delete("delete from sys_user_role where user_id = #{userId}")
    void deleteByAdminId(@Param("userId") Long adminId);
}
