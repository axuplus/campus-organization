package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Component
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\tsys_role_menu \n" +
            "WHERE\n" +
            "\trole_id IN ( SELECT role_id FROM sys_user_role WHERE user_id = #{userId} ) \n" +
            "\tAND menu_id = (\n" +
            "\tSELECT\n" +
            "\t\tid \n" +
            "\tFROM\n" +
            "\t\tsys_menu \n" +
            "\tWHERE\n" +
            "\tdescription = '全校数据')")
    SysRoleMenu checkShow(@Param("userId") Long userId);
}
