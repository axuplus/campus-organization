package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.MasterRoute;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

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
public interface MasterRouteMapper extends BaseMapper<MasterRoute> {

    @Delete("delete  from  master_route where master_id = #{masterId}")
    void deleteByMasterId(@Param("masterId") Long masterId);
}
