package com.guoshengkai.gpt.cn.eneity.vo;

import com.guoshengkai.gpt.cn.core.beans.PO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppFun extends PO {

    private boolean enable;

    private boolean enableGuest;

    private boolean enableGuestUpload;

    private boolean enableRegister;

    private boolean enableEmail;

}
