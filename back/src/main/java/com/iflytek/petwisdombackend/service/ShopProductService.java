package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.ShopProduct;
import com.iflytek.petwisdombackend.mapper.ShopProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductService {

    @Autowired
    private ShopProductMapper productMapper;

    public List<ShopProduct> getProducts(Long shopId) {
        return productMapper.findByShopId(shopId);
    }

    public boolean addProduct(ShopProduct product) {
        return productMapper.insert(product) > 0;
    }

    public boolean updateProduct(ShopProduct product) {
        return productMapper.update(product) > 0;
    }

    public boolean deleteProduct(Long id) {
        return productMapper.deleteById(id) > 0;
    }
}