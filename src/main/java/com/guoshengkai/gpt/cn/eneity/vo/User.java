package com.guoshengkai.gpt.cn.eneity.vo;

import com.guoshengkai.gpt.cn.core.annotation.po.ID;
import com.guoshengkai.gpt.cn.core.annotation.po.TableName;
import com.guoshengkai.gpt.cn.core.annotation.po.TempField;
import com.guoshengkai.gpt.cn.core.beans.PO;
import com.guoshengkai.gpt.cn.eneity.UserAttr;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User extends PO {

    private int id;

    private String username;

    private String email;

    private String password;

    private String avatar;

    private String nickname;

    private Date createTime;

    /**
     * 0 = 正常, 1 = 禁用
     */
    private int status;

    private String valiCode;

    private String token;

    private String accessKey;

    public void setAttr(UserAttr attr) {
        if (null == attr){
            return;
        }
        this.accessKey = attr.getAccessKey();
        this.nickname = attr.getNickname();
        this.avatar = attr.getAvatar();
    }

}
