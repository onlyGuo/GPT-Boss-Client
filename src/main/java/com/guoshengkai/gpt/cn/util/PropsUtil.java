package com.guoshengkai.gpt.cn.util;

import com.alibaba.fastjson.JSONObject;
import com.guoshengkai.gpt.cn.core.util.FileUtil;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PropsUtil {

    private static JSONObject props = null;


    public static void load() {
        System.setProperty("EVN.MYSQL.HOST", "");
        System.setProperty("EVN.MYSQL.DB_NAME", "");
        System.setProperty("EVN.MYSQL.USERNAME", "");
        System.setProperty("EVN.MYSQL.PASSWORD", "");

        System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
        System.setProperty("spring.datasource.url", "jdbc:mysql://${EVN.MYSQL.HOST}/${EVN.MYSQL.DB_NAME}?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true");
        System.setProperty("spring.datasource.username", "${EVN.MYSQL.USERNAME}");
        System.setProperty("spring.datasource.password", "${EVN.MYSQL.PASSWORD}");
        try {
            props = JSONObject.parseObject(FileUtil.readFile(new File("conf.json")));
            if (props.getString("System.inited").equals("TRUE")){
                System.setProperty("System.inited", "TRUE");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (props == null){
            props = new JSONObject();
            set("System.inited", "FALSE");
            String key = UUID.randomUUID().toString().replace("-", "");
            set("MANAGER_KEY", key);
        }
    }

    public static String get(String key){
        return System.getProperty(key, props.getString(key));
    }

    public static String get(String key, String defaultValue){
        return System.getProperty(key, null == props.getString(key) ? defaultValue : props.getString(key));
    }

    public static void set(String key, String value){
        if (key.equals("System.inited")){
            System.setProperty(key, value);
        }
        if (key.startsWith("EVN.")){
            System.setProperty(key, value);
        }else{
            props.put(key, value);
            FileUtil.writeFile(props.toJSONString().getBytes(StandardCharsets.UTF_8), new File("conf.json"));
        }
    }

    public static DataSource getDataSource() {
        String property = System.getProperty("EVN.MYSQL.HOST");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(System.getProperty("spring.datasource.url")
                .replace("${EVN.MYSQL.HOST}", System.getProperty("EVN.MYSQL.HOST"))
                .replace("${EVN.MYSQL.DB_NAME}", System.getProperty("EVN.MYSQL.DB_NAME")));
        dataSource.setUsername(System.getProperty("spring.datasource.username")
                .replace("${EVN.MYSQL.USERNAME}", System.getProperty("EVN.MYSQL.USERNAME")));
        dataSource.setPassword(System.getProperty("spring.datasource.password")
                .replace("${EVN.MYSQL.PASSWORD}", System.getProperty("EVN.MYSQL.PASSWORD")));
        dataSource.setDriverClassName(System.getProperty("spring.datasource.driver-class-name"));
        return dataSource;
    }
}
