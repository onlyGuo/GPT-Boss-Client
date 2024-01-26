package com.guoshengkai.gpt.cn.conf;

import com.guoshengkai.gpt.cn.core.beans.ResultBean;
import com.guoshengkai.gpt.cn.exception.AccessOAuthException;
import com.guoshengkai.gpt.cn.exception.AccessResourceOAuthException;
import com.guoshengkai.gpt.cn.exception.FlowException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 全局异常拦截与处理类
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/07 17:47
 */
@RestControllerAdvice
public class OverallExceptionHandler implements ResponseBodyAdvice<Object> {

    Logger logger = LoggerFactory.getLogger(OverallExceptionHandler.class);

    /**
     * MVCLogger对象托管容器,用于缓存未知异常的日志对象
     */
    private Map<String, Logger> loggerMap = new HashMap<>();

    /**
     * 获取发生异常方法中的Logger对象
     * @param clazz
     *      异常方法ClazzStr
     * @return
     */
    private Logger getLogger(String clazz){
        Logger logger = loggerMap.get(clazz);
        if (null == logger){
            logger = LoggerFactory.getLogger(clazz);
            loggerMap.put(clazz, logger);
        }
        return logger;
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResultBean handlerNoResourceFoundException(NoResourceFoundException e) {
        return ResultBean.error("没有找到资源:" + e.getResourcePath()).setCode(HttpStatus.NOT_FOUND.value());
    }


    /**
     * 未知异常空指针等
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResultBean exception(Exception e, HttpServletResponse httpResponse) {
        httpResponse.setStatus(500);
        String msg = "服务器内部错误:" + e.getClass();
        if (null != e.getMessage()){
            msg = e.getMessage();
        }
        String format = msg;
        getLogger(e.getStackTrace()[0].getClassName()).error(format, e);

        // 获取异常堆栈信息
        List<StackTraceElement> stackList = getStackList(e);
        Map<String, Object> stack = new HashMap<>();
        stack.put("exception", e.getClass());
        stack.put("stackList", stackList);

        return ResultBean.error(msg).setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).setResponseBody(stack);
    }

    /**
     * 参数校验异常400
     * @param e
     * @return
     */
    @ExceptionHandler(value = FlowException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResultBean validationException(Exception e) {
        logger.warn(e.getMessage());
        return ResultBean.error(e.getMessage()).setCode(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * 登录权限认证异常401
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessOAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResultBean accessOAuthException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        logger.warn(e.getMessage());
        String msg = e.getMessage();
        if (null == msg || "".equals(msg.trim())){
            msg = "没有权限";
        }
        return ResultBean.error(msg).setCode(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * 登录权限认证异常401
     * @param e
     * @return
     */
    @ExceptionHandler(value = AccessResourceOAuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResultBean accessOAuthExcetion2(Exception e, HttpServletRequest request, HttpServletResponse response){
        logger.warn("您没有该功能的操作权限");
        return ResultBean.error("您没有该功能的操作权限").setCode(HttpStatus.UNAUTHORIZED.value());
    }



    /**
     * 获取可能有帮助的异常堆栈信息
     * @param e
     * @return
     */
    private List<StackTraceElement> getStackList(Throwable e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        List<StackTraceElement> stackList = new ArrayList<>();
        for (StackTraceElement stackTraceElement: stackTrace){
            if (stackTraceElement.getClassName().contains("com.fbi")
                    || stackTraceElement.getClassName().contains("jdk.nashorn.internal.scripts.Script$Recompilation")){
                stackList.add(stackTraceElement);
            }
        }
        Throwable cause = e.getCause();
        if (null != cause){
            stackList.addAll(getStackList(cause));
        }
        return stackList;
    }

    /**
     * 当方法使用了ResponseBody注解时，自动封装返回结果
     * @param methodParameter
     *      方法返回参数
     * @param aClass
     *      转换类
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return AnnotatedElementUtils.hasAnnotation(methodParameter.getContainingClass(), ResponseBody.class)
                || methodParameter.hasMethodAnnotation(ResponseBody.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        if (o instanceof ResultBean){
            return o;
        }
        if (null == o){
            return ResultBean.success("ok");
        }else {
//            return ResultBean.success("ok").setResponseBody(o).setCode(200);
            return o;
        }
    }
}
