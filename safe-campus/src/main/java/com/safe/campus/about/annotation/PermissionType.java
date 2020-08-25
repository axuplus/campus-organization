package com.safe.campus.about.annotation;

public enum PermissionType {
    ADD("add"),             // 添加权限
    DEL("del"),             // 删除权限
    EDIT("edit"),           // 编辑权限
    QUERY("query"),         // 查看权限
    SET("set");             // 权限设置
    private String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
