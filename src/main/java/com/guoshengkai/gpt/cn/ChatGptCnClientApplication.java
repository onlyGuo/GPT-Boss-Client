package com.guoshengkai.gpt.cn;

import com.guoshengkai.gpt.cn.util.PropsUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatGptCnClientApplication {

    public static void main(String[] args) {
        PropsUtil.load();
        SpringApplication.run(ChatGptCnClientApplication.class, args);
    }



}
