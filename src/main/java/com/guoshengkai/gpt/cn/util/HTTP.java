package com.guoshengkai.gpt.cn.util;

import com.guoshengkai.gpt.cn.core.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static byte[] postByte(String url, Object data){
        return requestCommonByte(url, data, "POST");
    }

    private static byte[] requestCommonByte(String url, Object data, String method) {
        log.info("HTTP Request [{}]", url);
        Map header = new HashMap();
        String dataStr = null;
        Map dataMap = null;
        if(null != data){
            Map parse = JSON.parse(JSON.stringify(data), Map.class);
            if(parse.get("header") != null){
                header = JSON.parse(JSON.stringify(parse.get("header")), Map.class);
            }
            if (parse.get("data") != null){
                dataMap = JSON.parse(JSON.stringify(parse.get("data")), Map.class);
                dataStr = JSON.stringify(dataMap);
                log.info("HTTP Request Data: {}", dataStr);
            }
        }
        CloseableHttpClient client = HttpClients.createDefault();
        BaseRequest baseRequest = new BaseRequest(url, method);
        for (Object key: header.keySet()){
            baseRequest.setHeader(key.toString(), header.get(key).toString());
        }
        if (null != dataStr){
            if (header.get("FormData") != null){
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                        .create();
                for (Object key: dataMap.keySet()){
                    multipartEntityBuilder.addTextBody(key.toString(), dataMap.get(key).toString());
                }
                HttpEntity httpEntity = multipartEntityBuilder.build();
                baseRequest.setEntity(httpEntity);
            }else{
                baseRequest.setEntity(new StringEntity(dataStr, StandardCharsets.UTF_8));
            }
        }
        try (CloseableHttpResponse execute = client.execute(baseRequest)) {
            return execute.getEntity().getContent().readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static String getText(String url, Object data){
        return requestCommon(url, data, "GET");
    }

    public static String postText(String url, Object data){
        return requestCommon(url, data, "POST");
    }

    public static String putText(String url, Object data){
        return requestCommon(url, data, "PUT");
    }

    public static String deleteText(String url, Object data){
        return requestCommon(url, data, "DELETE");
    }

    @SneakyThrows
    private static String requestCommon(String url, Object data, String method){
        return new String(requestCommonByte(url, data, method), StandardCharsets.UTF_8);
    }

    public static byte[] getByte(String url, Object data) {
        return requestCommonByte(url, data, "GET");
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
    public static void postFetch(String url, Object data, FetchHandler handler){
        requestCommonFetch(url, data, "POST", handler);
    }

    public static void getFetch(String url, Object data, FetchHandler handler){
        requestCommonFetch(url, data, "GET", handler);
    }

    public static void putFetch(String url, Object data, FetchHandler handler){
        requestCommonFetch(url, data, "PUT", handler);
    }

    public static void deleteFetch(String url, Object data, FetchHandler handler){
        requestCommonFetch(url, data, "DELETE", handler);
    }

    private static void requestCommonFetch(String url, Object data, String method, FetchHandler handler) {
        log.info("HTTP Request [{}]", url);
        Map header = new HashMap();
        String dataStr = null;
        Map dataMap = null;
        if(null != data){
            Map parse = JSON.parse(JSON.stringify(data), Map.class);
            if(parse.get("header") != null){
                header = JSON.parse(JSON.stringify(parse.get("header")), Map.class);
            }
            if (parse.get("data") != null){
                dataMap = JSON.parse(JSON.stringify(parse.get("data")), Map.class);
                dataStr = JSON.stringify(dataMap);
                log.info("HTTP Request Data: {}", dataStr);
            }
        }
        CloseableHttpClient client = HttpClients.createDefault();
        BaseRequest baseRequest = new BaseRequest(url, method);
        for (Object key: header.keySet()){
            baseRequest.setHeader(key.toString(), header.get(key).toString());
        }
        if (null != dataStr){
            if (header.get("FormData") != null){
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                        .create();
                for (Object key: dataMap.keySet()){
                    multipartEntityBuilder.addTextBody(key.toString(), dataMap.get(key).toString());
                }
                HttpEntity httpEntity = multipartEntityBuilder.build();
                baseRequest.setEntity(httpEntity);
            } else if (baseRequest.getHeaders("Content-Type").length > 0 && baseRequest.getHeaders("Content-Type")[0].getValue().contains("application/x-www-form-urlencoded")) {
                StringBuilder stringBuilder = new StringBuilder();
                for (Object key: dataMap.keySet()){
                    stringBuilder.append(key.toString()).append("=").append(dataMap.get(key).toString()).append("&");
                }
                baseRequest.setEntity(new StringEntity(stringBuilder.toString(), StandardCharsets.UTF_8));
            } else{
                baseRequest.setEntity(new StringEntity(dataStr, StandardCharsets.UTF_8));
            }
        }
        BufferedReader reader = null;
        try (CloseableHttpResponse execute = client.execute(baseRequest)) {
            HttpEntity entity = execute.getEntity();
            if (execute.getStatusLine().getStatusCode() != 200){
                handler.handle(execute.getStatusLine().getStatusCode(), EntityUtils.toString(entity, StandardCharsets.UTF_8));
                return;
            }
            InputStream inputStream = entity.getContent();
            reader = new BufferedReader(new InputStreamReader(inputStream), 1);
            String line;
            while ((line = reader.readLine()) != null) {
                handler.handle(execute.getStatusLine().getStatusCode(), line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
