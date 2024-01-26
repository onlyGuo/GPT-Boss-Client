package com.guoshengkai.gpt.cn.client.action.impl;

import com.guoshengkai.gpt.cn.client.action.SocketController;
import com.guoshengkai.gpt.cn.client.action.SocketMapping;
import com.guoshengkai.gpt.cn.dao.ModelUseRecordDao;
import com.guoshengkai.gpt.cn.eneity.ModelUseRecord;
import jakarta.annotation.Resource;

@SocketController
public class ChatRecordAction {


    @Resource
    private ModelUseRecordDao modelUseRecordDao;

    @SocketMapping("chat/record")
    public void record(ModelUseRecord record) {
        modelUseRecordDao.add(record);
    }

}
