package com.safe.campus.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDto {
    private  Long roleId;
    private List<Long> menuIds;
}
