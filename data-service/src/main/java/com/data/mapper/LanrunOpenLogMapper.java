package com.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data.model.domain.LanrunOpenLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper
public interface LanrunOpenLogMapper extends BaseMapper<LanrunOpenLog> {
}
