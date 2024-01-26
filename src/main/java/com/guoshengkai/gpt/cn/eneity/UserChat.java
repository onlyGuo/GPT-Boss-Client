package com.guoshengkai.gpt.cn.eneity;

import com.guoshengkai.gpt.cn.core.annotation.po.FieldName;
import com.guoshengkai.gpt.cn.core.annotation.po.ID;
import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import com.guoshengkai.gpt.cn.core.annotation.po.TempField;
import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName(name = "USER_CHAT")
public class UserChat extends PO {

    @ID
    private int id;

    @FieldName(name = "USER_ID")
    private int userId;

    @FieldName(name = "NAME")
    private String name;

    @FieldName(name = "AVATAR")
    private String avatar;

    @FieldName(name = "DSCP")
    private String dscp;

    @FieldName(name = "CONTENT_LENGTH")
    private int contentLength;

    @FieldName(name = "LAST_MESSAGE")
    private String lastMessage;

    @FieldName(name = "LAST_MESSAGE_TIME")
    private Date lastMessageTime;

    @FieldName(name = "SYS_MSG")
    private String sysMsg;

    @FieldName(name = "MAX_TOKEN")
    private int maxToken;

    @FieldName(name = "TEMPERATURE")
    private double temperature;

    @FieldName(name = "PRESENCE_PENALTY")
    private double presence_penalty;

    @FieldName(name = "FREQUENCY_PENALTY")
    private double frequency_penalty;

    @FieldName(name = "CREATE_TIME")
    private Date createTime;

    @TempField
    private String lastMessageTimeStr;

    public String getLastMessageTimeStr() {
        if (null != lastMessageTime){
            // 刚刚、N分钟前、N小时前、N天前、N月前、N年前
            long time = new Date().getTime() - lastMessageTime.getTime();
            if (time < 60 * 1000){
                lastMessageTimeStr = "刚刚";
            }else if (time < 60 * 60 * 1000L){
                lastMessageTimeStr = time / (60 * 1000L) + "分钟前";
            }else if (time < 24 * 60 * 60 * 1000L){
                lastMessageTimeStr = time / (60 * 60 * 1000L) + "小时前";
            }else if (time < 30 * 24 * 60 * 60 * 1000L){
                lastMessageTimeStr = time / (24 * 60 * 60 * 1000L) + "天前";
            }else if (time < 12 * 30 * 24 * 60 * 60 * 1000L){
                lastMessageTimeStr = time / (30 * 24 * 60 * 60 * 1000L) + "月前";
            }else {
                lastMessageTimeStr = time / (12 * 30 * 24 * 60 * 60 * 1000L) + "年前";
            }
        }
        return lastMessageTimeStr;
    }
}
