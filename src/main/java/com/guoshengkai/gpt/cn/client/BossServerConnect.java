package com.guoshengkai.gpt.cn.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guoshengkai.gpt.cn.client.action.ControllerMethodMapper;
import com.guoshengkai.gpt.cn.client.action.SocketController;
import com.guoshengkai.gpt.cn.client.action.SocketMapping;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@ChannelHandler.Sharable
@Slf4j
public class BossServerConnect extends SimpleChannelInboundHandler<String> {
//    @Resource
//    private SpringBootApplicationUtil springBootApplicationUtil;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private BossSocketClient client;

    private Map<String, ControllerMethodMapper> actionMapper = new HashMap<>();

    @PostConstruct
    private void init(){
//        Map<String, Object> beansWithAnnotation = SpringBootApplicationUtil
//                .getApplicationContext().getBeansWithAnnotation(SocketController.class);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(SocketController.class);
        beansWithAnnotation.forEach((k, v) -> {
            Method[] methods = v.getClass().getDeclaredMethods();
            for (Method method: methods){
                SocketMapping annotation = method.getAnnotation(SocketMapping.class);
                if (annotation == null){
                    continue;
                }
                if (actionMapper.containsKey(annotation.value())){
                    throw new RuntimeException("The Socket Mapping key [" + annotation.value() + "] is exists!");
                }
                ControllerMethodMapper mapper = new ControllerMethodMapper();
                mapper.setBean(v);
                mapper.setMethod(method);
                actionMapper.put(annotation.value(), mapper);
            }
        });

        // 连接FViteBoss
        String host = System.getenv("ENV.BOSS_SOCKET_HOST") == null ?
                "icoding.ink:9999" : System.getenv("ENV.BOSS_SOCKET_HOST");
        String[] split = host.split(":");
        client.register(split[0], Integer.parseInt(split[1]), this);
    }

    public BossSocketClient getClient(){
        return client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        JSONObject jsonObject = JSON.parseObject(msg);
        String action = jsonObject.getString("r");
        ControllerMethodMapper mapper = actionMapper.get(action);
        if (mapper == null){
            throw new RuntimeException("The Socket Mapping key [" + action + "] is not exists!");
        }
        Parameter[] parameters = mapper.getMethod().getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++){
            Parameter parameter = parameters[i];
            if (parameter.getType() == ChannelHandlerContext.class){
                args[i] = ctx;
            }else if (parameter.getType() == BossSocketClient.class){
                args[i] = client;
            } else {
                args[i] = jsonObject.getObject("d", parameter.getType());
            }
        }
        try {
            mapper.getMethod().invoke(mapper.getBean(), args);
        }catch (Exception e){
            if (e instanceof InvocationTargetException ex){
                Throwable cause = ex.getCause();
                log.error(cause.getMessage(), cause);
            }else{
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ping(ctx.channel());
    }

    /**
     * 数据读取完毕
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * 异常处理，打印异常并关闭通道
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private Random random = new Random();
    private int baseRandom = 8;

    private void ping(Channel channel) {
        ScheduledFuture<?> future = channel.eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                if (channel.isActive()) {
                    channel.writeAndFlush(ClientIdleStateTrigger.HEART_BEAT);
                } else {
                    channel.closeFuture();
                    throw new RuntimeException();
                }
            }
        }, 4, TimeUnit.SECONDS);

        future.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                if (future.isSuccess()) {
                    ping(channel);
                }
            }
        });
    }

}
