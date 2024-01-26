package com.guoshengkai.gpt.cn.core;

import java.util.Map;

public interface CommAttr {

    interface APP_STATUS {
        /**
         * 离线
         */
        int OFFLINE = 0;
        /**
         * 在线
         */
        int INLINE = 1;

        /**
         * 待初始化
         */
        int NO_INIT = 2;

        /**
         * 初始化中
         */
        int INITING = 3;

        /**
         * 已关闭
         */
        int CLOSED = 4;
    }

}
