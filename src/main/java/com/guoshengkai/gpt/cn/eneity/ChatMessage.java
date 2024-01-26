package com.guoshengkai.gpt.cn.eneity;

import com.guoshengkai.gpt.cn.core.annotation.po.FieldName;
import com.guoshengkai.gpt.cn.core.annotation.po.ID;
import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName(name = "CHAT_MESSAGE")
public class ChatMessage extends PO {

    @ID
    private int id;

    @FieldName(name = "CHAT_ID")
    private int chatId;

    @FieldName(name = "USER_ID")
    private int userId;

    @FieldName(name = "CONTENT")
    private String content;

    @FieldName(name = "ROLE")
    private String role;

    @FieldName(name = "CREATE_TIME")
    private Date createTime;

    @FieldName(name = "MESSAGE_INDEX")
    private String index;

}
