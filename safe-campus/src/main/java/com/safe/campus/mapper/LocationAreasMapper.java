package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.LocationAreas;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 区县信息表 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-19
 */
@Component
@Mapper
public interface LocationAreasMapper extends BaseMapper<LocationAreas> {

}
