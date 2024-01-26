package com.guoshengkai.gpt.cn.ws;

import com.guoshengkai.gpt.cn.client.BossSocketClient;
import com.guoshengkai.gpt.cn.client.action.SocketParams;
import com.guoshengkai.gpt.cn.core.SpringBootApplicationUtil;
import com.guoshengkai.gpt.cn.core.util.DateUtil;
import com.guoshengkai.gpt.cn.core.util.cache.Key;
import com.guoshengkai.gpt.cn.core.util.cache.LocalCacheUtil;
import com.guoshengkai.gpt.cn.eneity.vo.User;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ServerEndpoint("/api/v1/user-session")
public class UserLoginSocket {
    private static final Map<Integer, Map<String, Session>> sessions = new HashMap<>();

    private BossSocketClient client = null;

    public BossSocketClient getClient() {
        if (null == client){
            client = SpringBootApplicationUtil.getBean(BossSocketClient.class);
        }
        return client;
    }

    @OnOpen
    public void onOpen(Session session) {
        session.setMaxIdleTimeout(1000 * 60 * 60 * 3);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if(message.equals("ping")){
            session.getAsyncRemote().sendText("pong");
        }else if (message.startsWith("login:")) {
            String[] split = message.split(":");
            String token = split[1];
            User user = LocalCacheUtil.get(Key.as("TOKEN", token), User.class);
            if (null == user){
                session.getAsyncRemote().sendText("login:fail");
                return;
            }
            Map<String, Session> map = sessions.get(user.getId());
            if (map == null) {
                map = new HashMap<>();
                sessions.put(user.getId(), map);
            }
            map.put(session.getId(), session);
            session.getUserProperties().put("userId", user.getId());
            session.getAsyncRemote().sendText("login:success");
            getClient().sendTo("user/login", SocketParams.of("userId", user.getId()));

            // 记录日活
            Key key = Key.as("USER_ACTIVE", DateUtil.formatPramm("yyyyMMdd"));
            Set<Integer> set = LocalCacheUtil.get(key, Set.class);
            if (null == set){
                set = new HashSet<>();
                LocalCacheUtil.put(key, set, TimeUnit.DAYS, 1);
            }
            set.add(user.getId());
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (null == session.getUserProperties().get("userId")){
            return;
        }
        Map<String, Session> userSessions = sessions.get(Integer.parseInt(session.getUserProperties().get("userId").toString()));
        if (null != userSessions) {
            // 移除当前session
            userSessions.remove(session.getId());
            if (userSessions.isEmpty()) {
                // 移除整个用户
                sessions.remove(Integer.parseInt(session.getUserProperties().get("userId").toString()));
                // 已退出登录
                log.info("用户{}退出登录", session.getUserProperties().get("userId"));
                getClient().sendTo("user/logout", SocketParams.of("userId", session.getUserProperties().get("userId")));
            }
        }
    }

    public void send(int uid, String message){
        Map<String, Session> userSessions = sessions.get(uid);
        if (null != userSessions) {
            userSessions.forEach((k, v) -> v.getAsyncRemote().sendText(message));
        }
    }

    public int onLineCount(){
        return sessions.size();
    }
}
