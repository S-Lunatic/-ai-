package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.ShopProduct;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopProductMapper {

    @Select("SELECT * FROM shop_product WHERE shop_id = #{shopId} ORDER BY created_at DESC")
    List<ShopProduct> findByShopId(Long shopId);

    @Insert("INSERT INTO shop_product(shop_id, name, price, unit, status) VALUES(#{shopId}, #{name}, #{price}, #{unit}, 'on')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ShopProduct product);

    @Update("UPDATE shop_product SET name = #{name}, price = #{price}, unit = #{unit}, status = #{status} WHERE id = #{id}")
    int update(ShopProduct product);

    @Delete("DELETE FROM shop_product WHERE id = #{id}")
    int deleteById(Long id);
}