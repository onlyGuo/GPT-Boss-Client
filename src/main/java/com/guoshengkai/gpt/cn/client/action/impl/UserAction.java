package com.guoshengkai.gpt.cn.client.action.impl;

import com.guoshengkai.gpt.cn.client.action.SocketController;
import com.guoshengkai.gpt.cn.client.action.SocketMapping;
import com.guoshengkai.gpt.cn.core.util.FileUtil;
import com.guoshengkai.gpt.cn.dao.SystemMessageDao;
import com.guoshengkai.gpt.cn.eneity.SystemMessage;
import com.guoshengkai.gpt.cn.eneity.vo.User;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import com.guoshengkai.gpt.cn.ws.UserLoginSocket;
import jakarta.annotation.Resource;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SocketController
public class UserAction {

    @Resource
    private UserLoginSocket userLoginSocket;

    @Resource
    private SystemMessageDao systemMessageDao;

    @SocketMapping("user/logout")
    public void logout(User user){
        userLoginSocket.send(user.getId(), "logout");
        System.out.println("logout");
    }

    @SocketMapping("user/notice")
    public void sendUserNotice(SystemMessage message){
        // 下载消息中的图片
        String messageText = message.getMessageText();
        List<String> imgList = new ArrayList<>();
        // 获取MD格式中的IMG
        String regex = "!\\[.*?\\]\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(messageText);
        List<String> imgs = new LinkedList<>();
        while (matcher.find()) {
            String imgUrl = matcher.group(1);
            imgs.add(imgUrl);
        }
        // 下载图片
        for (String img : imgs) {
            byte[] aByte = ServerUtil.getByte(img, null);
            FileUtil.writeFile(aByte, new File("data/files/" +
                    img.replace("api/v1/file/display/", "")));
        }

        message.setCreatedTime(new Date());
        systemMessageDao.add(message);
        userLoginSocket.send(message.getUserId(), "notice");
    }

}
