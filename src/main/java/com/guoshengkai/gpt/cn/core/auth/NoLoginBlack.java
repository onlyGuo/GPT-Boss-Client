package com.guoshengkai.gpt.cn.core.auth;

import java.lang.annotation.*;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * Controller方法中打入该注解, 表示不进行登录认证
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLoginBlack {

}
