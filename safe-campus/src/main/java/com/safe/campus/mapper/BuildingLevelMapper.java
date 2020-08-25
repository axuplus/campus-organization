package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingLevel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 楼层表 Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-08-06
 */
@Component
@Mapper
public interface BuildingLevelMapper extends BaseMapper<BuildingLevel> {

}
