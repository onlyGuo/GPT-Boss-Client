package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.core.beans.Method;
import com.guoshengkai.gpt.cn.core.beans.ResultPage;
import com.guoshengkai.gpt.cn.core.sql.where.C;
import com.guoshengkai.gpt.cn.dao.SystemMessageDao;
import com.guoshengkai.gpt.cn.eneity.SystemMessage;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/system-message")
public class SystemMessageController {

    @Resource
    private SystemMessageDao systemMessageDao;

    @GetMapping("list")
    public ResultPage<SystemMessage> listMyMessage(int page, int pageSize){
        return systemMessageDao.list(Method.where(SystemMessage::getUserId, C.EQ, ThreadUtil.getUserId())
                .orderBy("is_read asc, id desc"), page, pageSize);
    }

    @GetMapping("read/{messageId}")
    public void readMessage(@PathVariable int messageId){
        systemMessageDao.execute("UPDATE " + systemMessageDao.tableName() + " SET is_read = 1 " +
                "WHERE id = ? AND user_id = ?", messageId, ThreadUtil.getUserId());
    }

    @GetMapping("no-read-count")
    public Map<String, Long> countMyNoReadMessage(){
        long count = systemMessageDao.count(Method.where(SystemMessage::getUserId, C.EQ, ThreadUtil.getUserId())
                .and(SystemMessage::isRead, C.EQ, false));
        return Map.of("count", count);
    }

}
