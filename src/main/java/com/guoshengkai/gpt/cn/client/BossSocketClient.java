package com.guoshengkai.gpt.cn.client;

import com.alibaba.fastjson.JSON;
import com.guoshengkai.gpt.cn.client.action.SocketParams;
import com.guoshengkai.gpt.cn.util.PropsUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BossSocketClient {

    private static final ThreadGroup group = new ThreadGroup("GPT-Boss-Socket");

    private static final ThreadGroup activeGroup = new ThreadGroup("GPT-Boss-Socket-Active");
    private NioEventLoopGroup eventExecutors;
    private ChannelFuture channelFuture;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    boolean isRunning = false;

    /**
     * 注册到FVite
     * @param host
     *      FVite的IP地址
     * @param port
     *      FVite的端口
     * @param handler
     *      处理器
     */
    @SneakyThrows
    public void register(String host, int port, ChannelInboundHandlerAdapter handler) {
        if (channelFuture != null){
            shutdown();
        }
        isRunning = true;
        new Thread(group, () -> {
            while (isRunning){
                logger.info("Ready to register with GPT-Boss at: host={}, port={}", host, port);
                try {
                    //创建bootstrap对象，配置参数
                    Bootstrap bootstrap = new Bootstrap();
                    eventExecutors = new NioEventLoopGroup(1);
                    //设置线程组
                    bootstrap.group(eventExecutors)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    //添加客户端通道的处理器
                                    ch.pipeline()
                                            .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                                    0, 8, 0, 8))
                                            .addLast(new LengthFieldPrepender(8))
                                            .addLast(new StringDecoder())
                                            .addLast(new StringEncoder())
                                            .addLast(handler);
                                }
                            });
                    //连接服务端
                    channelFuture = bootstrap.connect(host, port).sync();
                    channelFuture.channel()
                            .writeAndFlush("::fv::reg::" + PropsUtil.get("MANAGER_KEY")).sync();
                    logger.info("Successfully registered to GPT-Boss!");
                    sendTo("/base/info");
                    //对通道关闭进行监听
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
//                    logger.error(e.getMessage(), e);
                } finally {
                    //关闭线程组
                    boolean oldState = isRunning;
                    if (channelFuture != null){
                        shutdown();
                    }
                    try {
                        eventExecutors.shutdownGracefully();
                    }catch (Exception e){
                        logger.error(e.getMessage(), e);
                    }
                    isRunning = oldState;
                }
                logger.info("GPT-Boss is not available, waiting for retry...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 关闭FVite的连接
     */
    public void shutdown(){
        isRunning = false;
        channelFuture.channel().close();
        channelFuture = null;
        eventExecutors.shutdownGracefully();
    }

    /**
     * 发送消息到FVite
     * @param content
     *      消息内容
     */
    private void send(Object content){
        if (channelFuture != null){
            channelFuture.channel().writeAndFlush(JSON.toJSONString(content));
        }
    }

    /**
     * 发送消息到FVite
     * @param action
     *      消息动作
     * @param params
     *      消息参数
     */
    public void sendTo(String action, SocketParams params){
        Map<String, Object> map = new HashMap<>();
        map.put("a", action);
        map.put("p", params.getParams());
        send(map);
    }

    /**
     * 发送消息到FVite
     * @param action
     *      消息动作
     */
    public void sendTo(String action){
        Map<String, Object> map = new HashMap<>();
        map.put("a", action);
        send(map);
    }
}
