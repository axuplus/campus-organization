package com.safe.campus.mapper;

;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SysFileDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface SysFileMapper extends BaseMapper<SysFileDomain> {

}
