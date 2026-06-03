package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.ShopEmployee;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShopEmployeeMapper {

    @Select("SELECT * FROM shop_employee WHERE shop_id = #{shopId} ORDER BY created_at DESC")
    List<ShopEmployee> findByShopId(Long shopId);

    @Insert("INSERT INTO shop_employee(shop_id, name, position, icon) VALUES(#{shopId}, #{name}, #{position}, #{icon})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ShopEmployee employee);

    @Delete("DELETE FROM shop_employee WHERE id = #{id}")
    int deleteById(Long id);
}