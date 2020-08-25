package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.LocationProvinces;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-19
 */
@Component
@Mapper
public interface LocationProvincesMapper extends BaseMapper<LocationProvinces> {

    @Select("select * from location_provinces")
    List<LocationProvinces> getPovinces();
}
