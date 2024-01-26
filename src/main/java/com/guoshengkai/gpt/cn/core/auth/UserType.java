package com.guoshengkai.gpt.cn.core.auth;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 用户类型枚举
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/27 10:13
 */
public enum UserType {
    /**
     * 平台管理员
     */
    PLAN("PLAN"),
    /**
     * 企业用户
     */
    COMPANY("COMPANY");

    private String name;

    UserType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
