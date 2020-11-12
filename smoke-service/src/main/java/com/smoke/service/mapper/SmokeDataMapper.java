package com.smoke.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smoke.service.model.entity.SmokeData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Joma
 * @since 2020-11-12
 */
@Mapper
@Component
public interface SmokeDataMapper extends BaseMapper<SmokeData> {

}
