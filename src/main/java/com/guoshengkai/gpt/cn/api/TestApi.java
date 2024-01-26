package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.conf.NoLogin;
import com.guoshengkai.gpt.cn.util.PropsUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestApi {

    @GetMapping("1")
    @NoLogin
    public Object test(){
        return Map.of("System.inited", PropsUtil.get("System.inited", "FALSE"));
    }

}
