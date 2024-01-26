package com.guoshengkai.gpt.cn.schedules;

import com.guoshengkai.gpt.cn.core.util.DateUtil;
import com.guoshengkai.gpt.cn.core.util.cache.Key;
import com.guoshengkai.gpt.cn.core.util.cache.LocalCacheUtil;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import com.guoshengkai.gpt.cn.ws.UserLoginSocket;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
public class StatsUserActiveSchedule {

    @Resource
    private UserLoginSocket userLoginSocket;

    @Scheduled(cron = "0 0 0/1 * * ?") // 每小时执行一次
    public void setActive(){
        Key key = Key.as("USER_ACTIVE", DateUtil.formatPramm("yyyyMMdd"));
        Set<Integer> set = LocalCacheUtil.get(key, Set.class);
        if (null == set){
            set = new HashSet<>();
        }
        int i = set.size();
        ServerUtil.put("api/v1/app/user/active-count", Map.of("count", i));
    }
}
