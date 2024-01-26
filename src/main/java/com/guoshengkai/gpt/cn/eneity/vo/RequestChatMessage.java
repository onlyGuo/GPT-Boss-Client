package com.guoshengkai.gpt.cn.eneity.vo;

import com.guoshengkai.gpt.cn.eneity.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestChatMessage {

    private String chatId;

    private List<ChatMessage> message;

    private String model;

    private double temperature = 0.7;

    private int presencePenalty = 0;

    private int frequencyPenalty = 0;

    private String sysMsg;

    private int userId;


}
