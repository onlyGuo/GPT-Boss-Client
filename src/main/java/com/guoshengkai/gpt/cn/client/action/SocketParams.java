package com.guoshengkai.gpt.cn.client.action;

import java.util.HashMap;
import java.util.Map;

public class SocketParams {
    Map<String, Object> params = new HashMap<>();
    public static SocketParams of(String key, Object value) {
        SocketParams params = new SocketParams();
        params.append(key, value);
        return params;
    }

    public SocketParams append(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }
}