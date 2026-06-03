package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.ShopEmployee;
import com.iflytek.petwisdombackend.service.ShopEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop/employee")
public class ShopEmployeeController {

    @Autowired
    private ShopEmployeeService employeeService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long shopId) {
        Map<String, Object> result = new HashMap<>();
        List<ShopEmployee> list = employeeService.getEmployees(shopId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ShopEmployee employee) {
        Map<String, Object> result = new HashMap<>();
        boolean success = employeeService.addEmployee(employee);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = employeeService.deleteEmployee(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}