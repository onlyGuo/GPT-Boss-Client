package com.guoshengkai.gpt.cn.client.action;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class ControllerMethodMapper {

    private Object bean;

    private Method method;

}