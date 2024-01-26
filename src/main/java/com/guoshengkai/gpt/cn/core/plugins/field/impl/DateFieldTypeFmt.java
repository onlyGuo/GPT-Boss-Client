package com.guoshengkai.gpt.cn.core.plugins.field.impl;

import com.guoshengkai.gpt.cn.core.SqlSourceConfig;
import com.guoshengkai.gpt.cn.core.enums.SqlType;
import com.guoshengkai.gpt.cn.core.plugins.field.FieldTypeFmt;
import com.guoshengkai.gpt.cn.core.plugins.field.PluginFieldValueType;
import com.guoshengkai.gpt.cn.core.util.DateUtil;

import java.util.Date;

/**
 * @Author: 郭胜凯
 * @Date: 2019-07-23 16:35
 * @Email 719348277@qq.com
 * @Description: 日期时间类型格式化为Sql支持的字符串
 */
@PluginFieldValueType(Date.class)
public class DateFieldTypeFmt implements FieldTypeFmt {

    @Override
    public String fmt(Object fieldValue) {
        if (null == fieldValue){
            return "null";
        }
        Date date = (Date) fieldValue;

        String dateStr = DateUtil.formatAll(date);

        if (SqlType.MYSQL == SqlSourceConfig.getType()){
            return "STR_TO_DATE(\'{dtStr}\', \'%Y-%m-%d %H:%i:%s\')".replace("{dtStr}", dateStr);
        }
        if (SqlType.ORACLE == SqlSourceConfig.getType()){
            return "TO_DATE(\'{dtStr}\', \'yyyy-mm-dd hh24:mi:ss\')".replace("{dtStr}", dateStr);
        }
        return dateStr;
    }
}
