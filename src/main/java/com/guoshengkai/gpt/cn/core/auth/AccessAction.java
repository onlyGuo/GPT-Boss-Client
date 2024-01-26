package com.guoshengkai.gpt.cn.core.auth;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 权限动作枚举
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/26 16:15
 */
public enum AccessAction {


    ADD("ADD", "新增"),
    SEE("SEE", "查看"),
    EDIT("EDIT", "编辑"),
    DEL("DEL", "删除"),
    DRAW("DRAW", "绘制区域"),
    IMPORT("IMPORT", "导入"),
    EXPORT("EXPORT", "下载/导出"),
    SUPER_EXEC("SUPER_EXEC", "督办"),
    EXEC("EXEC", "办理"),
    SWITCH("SWITCH", "启用/停用"),
    QR_CODE("QR_CODE", "二维码"),
    FROZEN("FROZEN", "冻结"),
    RESET("RESET", "重置"),
    PC_FUN("PC_FUN", "PC功能配置"),
    APP_FUN("APP_FUN", "APP功能配置"),
    NONE("NONE", "--");



    private String code;

    private String name;

    AccessAction(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
