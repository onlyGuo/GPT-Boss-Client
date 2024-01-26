package com.guoshengkai.gpt.cn.core.auth;

import java.lang.annotation.*;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * Controller方法中打入该注解, 表示该方法需要进行权限认证（移动端权限）
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:05
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MobileAccess {
    MobileAccessItem value();
}
