package com.guoshengkai.gpt.cn.client.action;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SocketMapping {
    String value();
}