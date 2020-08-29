package com.safe.campus.about.annotation;

public enum PermissionType {
    ADD("add"),             // 添加权限
    DEL("del"),             // 删除权限
    EDIT("edit"),           // 编辑权限
    QUERY("query"),         // 查看权限
    SET("set"),             // 角色设置
    ACTIVE("active");       // 教师停用启用（分配账号）
    private String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
