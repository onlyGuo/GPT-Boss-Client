package com.guoshengkai.gpt.cn.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author gsk
 * 拦截器配置
 */
@Configuration
public class InterceptorConfigurerAdapter implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestThreadFilterConf()).addPathPatterns("/**")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/error/**")
                .excludePathPatterns("/error");
    }

}
