package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Order;
import com.iflytek.petwisdombackend.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    // 生成订单号
    private String generateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + System.currentTimeMillis() % 1000;
    }

    // 创建订单
    public boolean createOrder(Order order) {
        order.setOrderNo(generateOrderNo());
        order.setStatus("pending");
        return orderMapper.insert(order) > 0;
    }

    // 获取用户订单
    public List<Order> getUserOrders(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    // 获取商家订单
    public List<Order> getShopOrders(String shopName) {
        return orderMapper.findByShopName(shopName);
    }

    // 获取订单详情
    public Order getOrderById(Long id) {
        return orderMapper.findById(id);
    }

    // 更新订单状态（商家确认/拒绝）
    public boolean updateOrderStatus(Long id, String status) {
        return orderMapper.updateStatus(id, status) > 0;
    }
}