package com.guoshengkai.gpt.cn.api;

import com.alibaba.fastjson.JSONObject;
import com.guoshengkai.gpt.cn.client.BossSocketClient;
import com.guoshengkai.gpt.cn.client.action.SocketParams;
import com.guoshengkai.gpt.cn.conf.NoLogin;
import com.guoshengkai.gpt.cn.core.beans.Method;
import com.guoshengkai.gpt.cn.core.sql.where.C;
import com.guoshengkai.gpt.cn.core.util.cache.Key;
import com.guoshengkai.gpt.cn.core.util.cache.LocalCacheUtil;
import com.guoshengkai.gpt.cn.dao.UserAttrDao;
import com.guoshengkai.gpt.cn.eneity.UserAttr;
import com.guoshengkai.gpt.cn.eneity.vo.User;
import com.guoshengkai.gpt.cn.exception.ValidationException;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Resource
    private UserAttrDao userAttrDao;

    @Resource
    private BossSocketClient bossSocketClient;

    /**
     * 发送注册验证码
     * @param user
     *      用户注册信息
     */
    @PostMapping("register-email-code")
    @NoLogin
    public void sendEmailRegisterCode(@RequestBody User user) {
        if (!StringUtils.hasText(user.getEmail())){
            throw new ValidationException("邮箱不能为空");
        }
        ServerUtil.post("api/v1/app/email/send-register-code", user);
    }

    @PostMapping("register")
    @NoLogin
    public void register(@RequestBody User user) {
        ServerUtil.post("api/v1/app/user/register", user);
        UserAttr userAttr = new UserAttr();
        userAttr.setUserId(user.getId());
        userAttr.setAccessKey(UUID.randomUUID().toString().replace("-", ""));
        userAttr.setAvatar(user.getAvatar());
        userAttr.setNickname(user.getNickname());
        userAttrDao.add(userAttr);
    }

    @PostMapping("login")
    @NoLogin
    public User login(@RequestBody User user) {
        JSONObject login = ServerUtil.post("api/v1/app/user/login", user);
        String token = login.getString("token");
        User loginUser = login.toJavaObject(User.class);
        loginUser.setAttr(userAttrDao.get(loginUser.getId()));
        LocalCacheUtil.put(Key.as("TOKEN", token), loginUser, TimeUnit.DAYS, 7);
        return loginUser;
    }

    @GetMapping("info")
    public User info(){
        return ThreadUtil.getUserEntity();
    }

    @PutMapping("accessKey")
    public Map<String, String> resetAccessKey(){
        UserAttr userAttr = userAttrDao.get(Method.where(UserAttr::getUserId, C.EQ, ThreadUtil.getUserId()));
        userAttr.setAccessKey(UUID.randomUUID().toString().replace("-", ""));
        userAttrDao.update(userAttr);
        return Map.of("accessKey", userAttr.getAccessKey());
    }

    @PutMapping("info")
    public User updateInfo(@RequestBody User user){
        UserAttr userAttr = userAttrDao.get(Method.where(UserAttr::getUserId, C.EQ, ThreadUtil.getUserId()));
        userAttr.setNickname(user.getNickname());
        userAttr.setAvatar(user.getAvatar());
        userAttrDao.update(userAttr);
        User user1 = ThreadUtil.getUserEntity();
        user1.setAttr(userAttr);
        if (StringUtils.hasText(user1.getNickname())){
            bossSocketClient.sendTo("user/put", SocketParams.of("id", user1.getId())
                    .append("nickname", user1.getNickname()));
        }
        return user1;
    }

    @GetMapping("balance")
    public Map<String, Object> getBalance(){
        return ServerUtil.get("api/v1/app/user/balance?userId=" + ThreadUtil.getUserId(), null);
    }

}
