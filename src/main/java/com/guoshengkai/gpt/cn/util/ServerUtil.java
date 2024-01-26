package com.guoshengkai.gpt.cn.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guoshengkai.gpt.cn.exception.ValidationException;

import java.util.Map;

public class ServerUtil {

    private static final String SERVER_URL = System.getenv("ENV.BOSS_SERVER_URL") == null ? "https://boss.icoding.ink/" : System.getenv("ENV.BOSS_SERVER_URL");

    public static JSONObject get(String uri, Object data) {
        String text = HTTP.getText(SERVER_URL + (SERVER_URL.endsWith("/") ? "" : "/") + uri,
                Map.of("header", Map.of(
                            "Content-Type", "application/json",
                            "Manager-Key", "Bearer " + PropsUtil.get("MANAGER_KEY")
                        ),
                        "data", data == null ? Map.of() : data)
        );
        JSONObject jsonObject = JSON.parseObject(text);
        if (jsonObject.getIntValue("code") != 0 && jsonObject.getIntValue("code") != 200) {
            throw new ValidationException(jsonObject.getString("message"));
        }
        return jsonObject;
    }

    public static JSONObject post(String uri, Object data) {
        String text = HTTP.postText(SERVER_URL + (SERVER_URL.endsWith("/") ? "" : "/") + uri,
                Map.of("header", Map.of(
                            "Content-Type", "application/json",
                            "Manager-Key", "Bearer " + PropsUtil.get("MANAGER_KEY")
                        ),
                        "data", data == null ? Map.of() : data)
        );
        JSONObject jsonObject = JSON.parseObject(text);
        if (jsonObject.getIntValue("code") != 0 && jsonObject.getIntValue("code") != 200) {
            throw new ValidationException(jsonObject.getString("message"));
        }
        return jsonObject;
    }

    public static void postStream(String uri, Object data, FetchHandler handler) {
        HTTP.postFetch(SERVER_URL + (SERVER_URL.endsWith("/") ? "" : "/") + uri,
                Map.of("header", Map.of(
                            "Content-Type", "application/json",
                            "Manager-Key", "Bearer " + PropsUtil.get("MANAGER_KEY")
                        ),
                        "data", data), handler);
    }

    public static JSONObject put(String uri, Object data) {
        String text = HTTP.putText(SERVER_URL + (SERVER_URL.endsWith("/") ? "" : "/") + uri,
                Map.of("header", Map.of(
                                "Content-Type", "application/json",
                                "Manager-Key", "Bearer " + PropsUtil.get("MANAGER_KEY")
                        ),
                        "data", data)
        );
        JSONObject jsonObject = JSON.parseObject(text);
        if (jsonObject.getIntValue("code") != 0 && jsonObject.getIntValue("code") != 200) {
            throw new ValidationException(jsonObject.getString("message"));
        }
        return jsonObject;
    }

    public static byte[] getByte(String uri, Object data) {
        return HTTP.getByte(SERVER_URL + (SERVER_URL.endsWith("/") ? "" : "/") + uri,
                Map.of()
        );
    }
}
