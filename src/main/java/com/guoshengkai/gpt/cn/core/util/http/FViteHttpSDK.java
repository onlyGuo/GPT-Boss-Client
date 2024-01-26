package com.guoshengkai.gpt.cn.core.util.http;

/**
 * 暴露给FViteEditor的HTTP操作类
 */
public class FViteHttpSDK {

    /**
     * 发送Get请求
     * @param url
     *      请求地址
     * @param data
     *      请求信息
     * @return
     *      响应信息
     */
    public Object get(String url, Object data){
        return HTTP.get(url, data);
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
    public Object post(String url, Object data){
        return HTTP.post(url, data);
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
    public Object delete(String url, Object data){
        return HTTP.delete(url, data);
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
    public Object put(String url, Object data){
        return HTTP.put(url, data);
    }
}
