package com.guoshengkai.gpt.cn.core.annotation.po;


import com.guoshengkai.gpt.cn.core.enums.BigFieldType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BigField {
    BigFieldType value() default BigFieldType.CLOB;
}
