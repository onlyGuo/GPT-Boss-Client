package com.guoshengkai.gpt.cn.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class JSON {

    @Resource
    private ObjectMapper jacksonObjectMapper;

    private static ObjectMapper mapper;

    @PostConstruct
    void init(){
        JSON.mapper = this.jacksonObjectMapper;
    }
    /**
     * JSON字符串转对象
     * @param json
     *      json字符串
     * @return
     *      对象
     */
    @SneakyThrows
    public static Object parse(String json){
        return mapper.readValue(json, Object.class);
    }

    /**
     * JSON字符串转指定对象
     * @param json
     *      json字符串
     * @return
     *      指定对象
     */
    @SneakyThrows
    public static<T> T parse(String json, Class<T> clazz){
        return mapper.readValue(json, clazz);
    }

    public static<T> List<T> parseList(String json, Class<T> clazz){
        List<T> list = new ArrayList<>();
        List<?> parse = parse(json, List.class);
        for (Object i: parse){
            T parse1 = JSON
                    .parse(JSON.stringify(i), clazz);
            list.add(parse1);
        }
        return list;
    }
    /**
     * 对象转JSON字符串
     * @param json
     *      对象
     * @return
     *      json字符串
     */
    @SneakyThrows
    public static String stringify(Object json){
        if (null == json){
            return null;
        }
        if(json.getClass().getName().equals("jdk.nashorn.api.scripting.ScriptObjectMirror")){
            String s = mapper.writeValueAsString(json);
            if(s.contains("{\"0\":") && s.startsWith("{")){
                Map parse = JSON.parse(s, Map.class);
                return stringify(toJavaList(parse));
            }
            return s;
        }
        return mapper.writeValueAsString(json);
    }

    public static Object toJavaList(Object o){
        if (!(o instanceof Map)){
            return o;
        }
        Map map = (Map) o;
        boolean isArray = !map.isEmpty();
        List<Object> list = new ArrayList<>();
        for (Object key: map.keySet()){
            try {
                Integer.parseInt(key.toString());
            }catch (NumberFormatException e){
                isArray = false;
                break;
            }
            list.add(toJavaList(map.get(key)));
        }
        if (isArray){
            return list;
        }
        for (Object key: map.keySet()){
            map.put(key, toJavaList(map.get(key)));
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "test");
        map.put("v", "test2");
        Map<String, Object> mapc = new HashMap<>();
        mapc.put("0", "aaaa");
        mapc.put("1", "bbbb");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("aaa", "ccc");
        map2.put("11", "vvv");
        mapc.put("2", map2);
        map.put("c", mapc);
        System.out.println(toJavaList(map));
    }

}
