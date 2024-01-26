package com.guoshengkai.gpt.cn.eneity;

import com.guoshengkai.gpt.cn.core.annotation.po.*;
import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@TableName(name = "SYSTEM_MESSAGE")
public class SystemMessage extends PO {

    @ID
    private int id;

    @FieldName(name = "user_id")
    private int userId;

    /**
     * 0 = 系统， 1 = 帖子，2 = 用户
     */
    @FieldName(name = "message_type")
    private int messageType;

    @FieldName(name = "message_title")
    private String messageTitle;

    @FieldName(name = "business_id")
    private Integer businessId;

    @FieldName(name = "message_text")
    @LongTextField
    private String messageText;

    @FieldName(name = "created_time")
    private Date createdTime;

    @FieldName(name = "is_read")
    private boolean read;

    @FieldName(name = "form_id")
    private int formId;

    @TempField
    private String avatar;

}
