package com.guoshengkai.gpt.cn.conf;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright (c) 2021 HEBEI CLOUD IOT FACTORY BIGDATA CO.,LTD.
 * Legal liability shall be investigated for unauthorized use
 *
 * 全局Rest full API Date类型JSON 格式化
 * @Author: Guo Shengkai
 * @Date: Create in 2021/04/08 9:31
 */
@JsonComponent
@Slf4j
public class DateFormatConfig {

    /**
     * 日期格式化
     */
    public static class DateJsonSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonGenerator.writeString(dateFormat.format(date));
        }
    }

    /**
     * 解析日期字符串
     */
    public static class DateJsonDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            String dateStr = jsonParser.getText();
            if (StringUtils.isEmpty(dateStr)){
                return null;
            }
            if (dateStr.length() < 11){
                dateStr += " 00:00:00";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Long 类型传给前端时转为String
     */
    public static class LongJsonSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long aLong, JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(String.valueOf(aLong));
        }
    }

    /**
     * 解析Long类型字符串
     */
    public static class LongJsonDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            String str = jsonParser.getValueAsString();
            if (str == null){
                return null;
            }
            return Long.parseLong(str);
        }
    }

//    /**
//     * Integer 类型传给前端时转为String
//     */
//    public static class IntegerJsonSerializer extends JsonSerializer<Integer> {
//        @Override
//        public void serialize(Integer aLong, JsonGenerator jsonGenerator,
//                              SerializerProvider serializerProvider) throws IOException {
//            log.info("aaaaaaaaaaaaaa");
//            jsonGenerator.writeNumber(aLong);
//        }
//    }

//    public static class JsDataJsonSerializer extends JsonSerializer<Map> {
//
//        @Override
//        public void serialize(Map map, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//            jsonGenerator.writeObject(map);
//            log.info("JS Object to Json ===> {}", map);
//        }
//    }
}
