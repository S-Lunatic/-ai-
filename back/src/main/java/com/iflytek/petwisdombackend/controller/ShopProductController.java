package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.ShopProduct;
import com.iflytek.petwisdombackend.service.ShopProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop/product")
public class ShopProductController {

    @Autowired
    private ShopProductService productService;

    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long shopId) {
        Map<String, Object> result = new HashMap<>();
        List<ShopProduct> list = productService.getProducts(shopId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody ShopProduct product) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.addProduct(product);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody ShopProduct product) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.updateProduct(product);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = productService.deleteProduct(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}