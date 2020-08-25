package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.RouteConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 配置模块 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-19
 */
@Component
@Mapper
public interface RouteConfMapper extends BaseMapper<RouteConf> {

    @Select("select * from route_conf")
    List<RouteConf> getAllRoutes();

}
