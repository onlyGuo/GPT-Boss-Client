package com.guoshengkai.gpt.cn.core.auth;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 登录客户端类型枚举，每个类型对应自己专属的的TOKEN过期时间
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:08
 */
public enum Client {

    /**
     * WEB端登录(30分钟)
     */
    WEB("WEB", 60 * 30),
    /**
     * 移动端登录(60天)
     */
    MOBILE("MOBILE", 60 * 60 * 24 * 60);

    private String clientName;

    private long tokenTime;

    Client(String clientName, long tokenTime){
        this.clientName = clientName;
        this.tokenTime = tokenTime;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getTokenTime() {
        return tokenTime;
    }

    public void setTokenTime(long tokenTime) {
        this.tokenTime = tokenTime;
    }
}
