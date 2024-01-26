package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.core.Global;
import com.guoshengkai.gpt.cn.exception.ValidationException;
import com.guoshengkai.gpt.cn.util.ServerUtil;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @PostMapping("create")
    public Object createOrder(@RequestBody Map<String, Object> orderParam){
        return ServerUtil.post("api/v1/order/create-user-order", orderParam);
    }

    @GetMapping("my-orders")
    public Object listMyOrders(){
        return ServerUtil.get("api/v1/order/list-user-orders?page=1&pageSize=100&userId=" + ThreadUtil.getUserId(), null);
    }
}
