package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 权限菜单 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-17
 */
@Component
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\tsys_menu \n" +
            "WHERE\n" +
            "\tid = (\n" +
            "\tSELECT\n" +
            "\t\tmenu_id \n" +
            "\tFROM\n" +
            "\t\tsys_role_menu \n" +
            "\tWHERE\n" +
            "\trole_id = ( SELECT role_id FROM sys_user_role WHERE user_id = #{userId} )) \n" +
            "\tAND menu_name = #{menuName} \n" +
            "\tAND type = #{type}")
    SysMenu checkHavePermission(@Param("userId") Long userId, @Param("menuName") String menuName, @Param("type") Integer type);

}
