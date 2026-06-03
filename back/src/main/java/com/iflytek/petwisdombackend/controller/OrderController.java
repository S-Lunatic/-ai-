package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Order;
import com.iflytek.petwisdombackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 创建订单
    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();
        boolean success = orderService.createOrder(order);
        if (success) {
            result.put("code", 200);
            result.put("message", "预约成功");
            result.put("data", order);
        } else {
            result.put("code", 500);
            result.put("message", "预约失败");
        }
        return result;
    }

    // 获取用户订单
    @GetMapping("/user")
    public Map<String, Object> getUserOrders(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Order> list = orderService.getUserOrders(userId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取商家订单
    @GetMapping("/shop")
    public Map<String, Object> getShopOrders(@RequestParam String shopName) {
        Map<String, Object> result = new HashMap<>();
        List<Order> list = orderService.getShopOrders(shopName);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 更新订单状态（商家确认/完成）
    @PostMapping("/update-status")
    public Map<String, Object> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();

        Map<String, Object> result = new HashMap<>();
        boolean success = orderService.updateOrderStatus(id, status);
        if (success) {
            result.put("code", 200);
            result.put("message", "状态更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "状态更新失败");
        }
        return result;
    }
}