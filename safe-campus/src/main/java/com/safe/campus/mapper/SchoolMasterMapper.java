package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.SchoolMaster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SchoolMasterMapper extends BaseMapper<SchoolMaster> {

    @Select("select * from school_master")
    List<SchoolMaster> getSchoolMater();
}
