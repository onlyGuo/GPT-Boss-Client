package com.guoshengkai.gpt.cn.core.util.http;

import com.guoshengkai.gpt.cn.core.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Http 快捷操作类
 */
@Slf4j
public class HTTP {

    /**
     * 发送Get请求
     * @param url
     *      请求地址
     * @param data
     *      请求信息
     * @return
     *      响应信息
     */
    public static Object get(String url, Object data){
        return JSON.parse(requestCommon(url, data, "GET"));
    }

    /**
     * 发送POST请求
     * @param url
     *      请求地址
     * @param data
     *      请求信息
     * @return
     *      响应信息
     */
    public static Object post(String url, Object data){
        return JSON.parse(requestCommon(url, data, "POST"));
    }

    /**
     * 发送DELETE请求
     * @param url
     *      请求地址
     * @param data
     *      请求信息
     * @return
     *      响应信息
     */
    public static Object delete(String url, Object data){
        return JSON.parse(requestCommon(url, data, "DELETE"));
    }

    /**
     * 发送PUT请求
     * @param url
     *      请求地址
     * @param data
     *      请求信息
     * @return
     *      响应信息
     */
    public static Object put(String url, Object data){
        return JSON.parse(requestCommon(url, data, "PUT"));
    }


    @SneakyThrows
    private static String requestCommon(String url, Object data, String method){
        log.info("HTTP Request [{}]", url);
        Map header = new HashMap();
        String dataStr = null;
        if(null != data){
            Map parse = JSON.parse(JSON.stringify(data), Map.class);
            if(parse.get("header") != null){
                header = JSON.parse(JSON.stringify(parse.get("header")), Map.class);
            }
            if (parse.get("data") != null){
                dataStr = JSON.stringify(parse.get("data"));
                log.info("HTTP Request Data: {}", dataStr);
            }
        }
        CloseableHttpClient client = HttpClients.createDefault();
        BaseRequest baseRequest = new BaseRequest(url, method);
        for (Object key: header.keySet()){
            baseRequest.setHeader(key.toString(), header.get(key).toString());
        }
        if (null != dataStr){
            baseRequest.setEntity(new StringEntity(dataStr, StandardCharsets.UTF_8));
        }
        try (CloseableHttpResponse execute = client.execute(baseRequest)) {
            return EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
        }
    }

    static class BaseRequest extends HttpEntityEnclosingRequestBase{
        private final String METHOD_NAME;

        public BaseRequest(String uri, String methodName) {
            this.setURI(URI.create(uri));
            METHOD_NAME = methodName;
        }

        public String getMethod() {
            return METHOD_NAME;
        }
    }
}
