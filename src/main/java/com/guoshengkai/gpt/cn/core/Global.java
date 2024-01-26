package com.guoshengkai.gpt.cn.core;

import com.guoshengkai.gpt.cn.eneity.vo.AppFun;
import com.guoshengkai.gpt.cn.eneity.vo.Model;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Global {

    private static final Map<String, Object> map = new ConcurrentHashMap<>();

    public static<T> T get(String key, Class<T> clazz) {
        return map.get(key) == null ? null : (T) map.get(key);
    }

    public static void put(String key, Object value) {
        map.put(key, value);
    }

    public static List<Model> models;

    public static AppFun fun;

}
