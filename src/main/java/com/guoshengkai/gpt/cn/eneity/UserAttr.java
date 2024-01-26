package com.guoshengkai.gpt.cn.eneity;

import com.guoshengkai.gpt.cn.core.annotation.po.FieldName;
import com.guoshengkai.gpt.cn.core.annotation.po.ID;
import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(name = "USER_ATTR")
public class UserAttr extends PO {

    @ID
    private int id;

    @FieldName(name = "USER_ID")
    private int userId;

    @FieldName(name = "ACCESS_KEY")
    private String accessKey;

    @FieldName(name = "NICKNAME")
    private String nickname;

    @FieldName(name = "AVATAR")
    private String avatar;

}
