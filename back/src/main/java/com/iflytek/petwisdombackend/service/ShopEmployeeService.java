package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.ShopEmployee;
import com.iflytek.petwisdombackend.mapper.ShopEmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopEmployeeService {

    @Autowired
    private ShopEmployeeMapper employeeMapper;

    public List<ShopEmployee> getEmployees(Long shopId) {
        return employeeMapper.findByShopId(shopId);
    }

    public boolean addEmployee(ShopEmployee employee) {
        return employeeMapper.insert(employee) > 0;
    }

    public boolean deleteEmployee(Long id) {
        return employeeMapper.deleteById(id) > 0;
    }
}