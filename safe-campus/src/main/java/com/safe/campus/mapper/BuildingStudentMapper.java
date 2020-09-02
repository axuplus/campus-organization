package com.safe.campus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.safe.campus.model.domain.BuildingStudent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface BuildingStudentMapper extends BaseMapper<BuildingStudent> {

}
