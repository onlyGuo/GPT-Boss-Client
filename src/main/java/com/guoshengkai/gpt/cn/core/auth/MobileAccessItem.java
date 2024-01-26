package com.guoshengkai.gpt.cn.core.auth;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 权限枚举(手机端)
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:05
 */
public enum MobileAccessItem {

    TROUBLE_INPUT("TROUBLE_INPUT","隐患填报"),

    PHOTO("PHOTO","随手拍"),

    SAFE_PROMISE("SAFE_PROMISE","安全承诺"),

    MESSAGE("MESSAGE","消息"),

    PENDING("PENDING","待处理隐患"),

    RECTIFIED("RECTIFIED","待整改隐患"),

    REVIEW("REVIEW","待复查隐患"),

    DELAY_APPLY("DELAY_APPLY", "延期申请"),

    CHECK_TASK("CHECK_TASK", "检查任务"),

    PHOTO_CHECK("PHOTO_CHECK", "随手拍审核"),

    STATISTICS("STATISTICS", "统计"),

    ;


    private String value;
    private String name;

    MobileAccessItem(String value, String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
