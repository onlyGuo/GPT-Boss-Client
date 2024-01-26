package com.guoshengkai.gpt.cn.conf;

import com.guoshengkai.gpt.cn.core.util.cache.Key;
import com.guoshengkai.gpt.cn.core.util.cache.LocalCacheUtil;
import com.guoshengkai.gpt.cn.eneity.vo.User;
import com.guoshengkai.gpt.cn.exception.AccessOAuthException;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:46
 */
public class RequestThreadFilterConf implements HandlerInterceptor {


    protected Logger logger = LoggerFactory.getLogger(RequestThreadFilterConf.class);

    Map<String, List<Date>> requestMap = new HashMap<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String reqHeader = request.getHeader("Access-Control-Request-Headers");
        response.setStatus(HttpStatus.OK.value());
        if (reqHeader != null){
            response.setHeader("Access-Control-Allow-Headers", reqHeader);
        }
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            response.setStatus(204);
            return false;
        }
        if (handler instanceof HandlerMethod method) {
            NoInit noInit = method.getMethodAnnotation(NoInit.class);
            if (null == noInit){
                // 先判断系统是否完成初始化，否则返回406
                if ("FALSE".equals(System.getProperty("System.inited", "FALSE"))){
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    return false;
                }
            }
        }




        String token = getRequestToken(request);
        User user = null;
        // 解析token
        if (!StringUtils.isEmpty(token)){
            ThreadUtil.setToken(token);
            user = LocalCacheUtil.get(Key.as("TOKEN", token), User.class);
            ThreadUtil.setUserEntity(user);
            if (user != null){
                ThreadUtil.setUserId(user.getId());
                LocalCacheUtil.put(Key.as("TOKEN", token), user, TimeUnit.DAYS, 7);
            }
        }
        if (handler instanceof HandlerMethod method) {
            NoLogin methodAnnotation = method.getMethodAnnotation(NoLogin.class);
            if (null == methodAnnotation){
                if (null == token){
                    throw new AccessOAuthException("请先登录");
                }
                if (null == user){
                    throw new AccessOAuthException("登录信息已过期，请重新登录");
                }
            }
            NoInit noInit = method.getMethodAnnotation(NoInit.class);
            if (null == noInit){
                // 先判断系统是否完成初始化，否则返回406
                if ("FALSE".equals(System.getProperty("System.inited", "FALSE"))){
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    return false;
                }
            }
        }

        // 初始化上下文
        return true;
    }


    /**
     * 获取请求信息中的Token
     * @param request
     *      请求信息
     * @return
     */
    private String getRequestToken(HttpServletRequest request){
        String token = request.getHeader("token");

        if (StringUtils.isEmpty(token)){
            token = request.getHeader("access_token");
        }
        if (StringUtils.isEmpty(token)){
            token = request.getHeader("access-token");
        }
        if (StringUtils.isEmpty(token)){
            token = request.getHeader("authorization");
        }

        if (StringUtils.isEmpty(token)){
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)){
            token = request.getParameter("access_token");
        }
        if (StringUtils.isEmpty(token)){
            token = request.getParameter("access-token");
        }

        if (!StringUtils.isEmpty(token) && token.startsWith("Bearer")){
            token = token.substring(7);
        }
        return token;
    }
}
