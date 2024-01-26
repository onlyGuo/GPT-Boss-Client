package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.conf.NoLogin;
import com.guoshengkai.gpt.cn.core.Global;
import com.guoshengkai.gpt.cn.core.JSON;
import com.guoshengkai.gpt.cn.core.beans.Method;
import com.guoshengkai.gpt.cn.core.beans.ResultPage;
import com.guoshengkai.gpt.cn.core.beans.Sort;
import com.guoshengkai.gpt.cn.core.enums.OrderBy;
import com.guoshengkai.gpt.cn.core.sql.where.C;
import com.guoshengkai.gpt.cn.dao.ChatMessageDao;
import com.guoshengkai.gpt.cn.dao.ModelUseRecordDao;
import com.guoshengkai.gpt.cn.dao.UserChatDao;
import com.guoshengkai.gpt.cn.eneity.ChatMessage;
import com.guoshengkai.gpt.cn.eneity.ModelUseRecord;
import com.guoshengkai.gpt.cn.eneity.UserChat;
import com.guoshengkai.gpt.cn.eneity.vo.Model;
import com.guoshengkai.gpt.cn.eneity.vo.RequestChatMessage;
import com.guoshengkai.gpt.cn.eneity.vo.User;
import com.guoshengkai.gpt.cn.exception.ValidationException;
import com.guoshengkai.gpt.cn.util.DateUtil;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/chat")
public class ChatController {

    @Resource
    private UserChatDao userChatDao;

    @Resource
    private ChatMessageDao chatMessageDao;

    @Resource
    private ModelUseRecordDao modelUseRecordDao;

    @GetMapping
    public List<UserChat> listMyChat(){
        List<UserChat> list = userChatDao.list(Method.where(UserChat::getUserId, C.EQ, ThreadUtil.getUserId()));
        if (list.isEmpty()){
            list.add(newChat());
        }
        return list;
    }

    @PostMapping("new")
    public UserChat newChat(){
        String title = "未命名会话";
        UserChat dbChat = userChatDao.get(Method.where(UserChat::getName, C.START_WITH, title)
                .and(UserChat::getUserId, C.EQ, ThreadUtil.getUserId())
                .orderBy(Sort.of(UserChat::getId, OrderBy.DESC)).limit(0, 1));
        if (null != dbChat){
            String str = "";
            String temp = dbChat.getName().replace("(", "").replace(")", "").replace(title, "").trim();
            for (int i = 0; i < temp.length(); i++) {
                char c = temp.charAt(i);
                if (c >= '0' && c <= '9'){
                    str += c;
                }else {
                    break;
                }
            }
            if (str.isEmpty()){
                title += "(1)";
            }else{
                title += "(" + (Integer.parseInt(str) + 1) + ")";
            }
        }
        UserChat chat = new UserChat();
        chat.setName(title);
        chat.setCreateTime(new Date());
        chat.setContentLength(5);
        chat.setUserId(ThreadUtil.getUserId());
        chat.setFrequency_penalty(0.5);
        chat.setPresence_penalty(1);
        chat.setTemperature(0.7);
        chat.setMaxToken(4096);
        userChatDao.add(chat);
        return chat;
    }

    @PutMapping
    public void saveChat(@RequestBody UserChat chat){
        UserChat chat1 = userChatDao.get(chat.getId());
        if (null == chat1){
            throw new ValidationException("会话不存在");
        }
        if (chat1.getUserId() != ThreadUtil.getUserId()){
            throw new ValidationException("非法操作");
        }
        userChatDao.update(chat);
    }

    @GetMapping("message")
    public List<ChatMessage> listChatMessageByChatId(int chatId) {
        UserChat chat = userChatDao.get(chatId);
        if (null == chat){
            throw new ValidationException("会话不存在");
        }
        if (chat.getUserId() != ThreadUtil.getUserId()){
            throw new ValidationException("非法操作");
        }
        List<ChatMessage> list = chatMessageDao.list(Method.where(ChatMessage::getChatId, C.EQ, chatId)
                .orderBy(Sort.of(ChatMessage::getId, OrderBy.DESC)).limit(0, 50));
        // 反转
        for (int i = 0; i < list.size() / 2; i++) {
            ChatMessage temp = list.get(i);
            list.set(i, list.get(list.size() - i - 1));
            list.set(list.size() - i - 1, temp);
        }
        return list;
    }

    @PostMapping("message")
    @NoLogin
    public void sendChatMessage(@RequestBody RequestChatMessage chatMessage, HttpServletResponse response) {
        if (chatMessage.getMessage().isEmpty()){
            throw new ValidationException("不能发送空消息");
        }
        if (ThreadUtil.getUserId() != null && ThreadUtil.getUserId() > 0){
            UserChat chat = userChatDao.get(Integer.parseInt(chatMessage.getChatId()));
            if (null == chat){
                throw new ValidationException("会话不存在");
            }
            if (chat.getUserId() != ThreadUtil.getUserId()){
                throw new ValidationException("非法操作");
            }
            ChatMessage chatMessage1 = chatMessage.getMessage().get(chatMessage.getMessage().size() - 1);
            chatMessage1.setUserId(ThreadUtil.getUserId());
            chatMessage1.setCreateTime(new Date());
            chatMessage1.setRole("user");
            chatMessage1.setChatId(Integer.parseInt(chatMessage.getChatId()));
            chatMessage1.setIndex(UUID.randomUUID().toString().replace("-", ""));
            chatMessageDao.add(chatMessage1);
            chatMessage.setUserId(ThreadUtil.getUserId());
        }

        String index = UUID.randomUUID().toString().replace("-", "");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/event-stream;charset=utf-8");
        response.setHeader("message.index", index);
        response.setHeader("message.time", DateUtil.formatAll());

        try (PrintWriter writer = response.getWriter()){
            if (!Global.fun.isEnableGuest() && ThreadUtil.getUserEntity() == null){
                writeMessage(writer, index, "该网站已关闭游客访问，请<a href=\"javascript:login();\">登陆</a>后继续");
                return;
            }

            // 调用GPT
            StringBuilder replyMessageBuilder = new StringBuilder();
            ServerUtil.postStream("api/v1/gpt/chat", chatMessage, (code, message) -> {
                if (code == 200){
                    String msg = JSON.parse(message, Map.class).get("msg").toString();
                    replyMessageBuilder.append(msg);
                    writeMessage(writer, index, msg);
                }else{
                    if (message.contains("余额不足，请充值")){
                        String msg = "该模型为付费模型，您的余额不足，请前往<a href=\"javascript:goto('/user/purse');\">我的钱包</a>进行充值";
                        replyMessageBuilder.append(msg);
                        writeMessage(writer, index, msg);
                    }else{
                        replyMessageBuilder.append(writeError(writer, index, code, message));
                    }
                }
            });
            if (ThreadUtil.getUserId() != null && ThreadUtil.getUserId() > 0){
                ChatMessage msg = new ChatMessage();
                msg.setChatId(Integer.parseInt(chatMessage.getChatId()));
                msg.setUserId(0);
                msg.setCreateTime(new Date());
                msg.setRole("assistant");
                msg.setContent(replyMessageBuilder.toString());
                msg.setIndex(index);
                chatMessageDao.add(msg);
            }
        }catch (IOException e){
            throw new ValidationException(e.getMessage(), e);
        }
    }

    private void writeMessage(PrintWriter writer, String index, String message){
        writer.write(
                message
        );
        writer.flush();
    }

    private String writeError(PrintWriter writer, String index, int code, String message){
        message = message.trim();
        if (message.startsWith("````json") && message.endsWith("````")){
            message = message.substring(8, message.length() - 4).trim();
        }
        try {
            JSON.parse(message);
            String msg = """
                ```json
                $MESSAGE$
                ```
                """.replace("$MESSAGE$", message);
            writeMessage(writer, index, msg);
            return msg;
        }catch (Exception e){
            String msg = """
                ```json
                {
                    "code": $CODE$,
                    "message": $MESSAGE$
                }
                ```
                """.replace("$CODE$", String.valueOf(code))
                    .replace("$MESSAGE$", JSON.stringify(message));
            writeMessage(writer, index, msg);
            return msg;
        }
    }

    @GetMapping("tokens")
    public ResultPage<ModelUseRecord> listUseTokens(int page, int limit){
        return modelUseRecordDao.list(Method.where(ModelUseRecord::getUserId, C.EQ, ThreadUtil.getUserId())
                .and(ModelUseRecord::getCreateTime, C.DA, DateUtil.addDay(new Date(), -30))
                .orderBy(Sort.of(ModelUseRecord::getId, OrderBy.DESC)), page, limit);
    }

}
