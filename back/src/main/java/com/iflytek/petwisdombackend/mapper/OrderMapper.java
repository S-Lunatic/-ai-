package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    // 创建订单
    @Insert("INSERT INTO `order`(order_no, user_id, shop_name, service_name, pet_id, appoint_time, remark, status) " +
            "VALUES(#{orderNo}, #{userId}, #{shopName}, #{serviceName}, #{petId}, #{appointTime}, #{remark}, 'pending')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    // 查询用户的订单
    @Select("SELECT o.*, p.name as petName, p.species as petSpecies " +
            "FROM `order` o " +
            "LEFT JOIN pet p ON o.pet_id = p.id " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.created_at DESC")
    List<Order> findByUserId(Long userId);

    // 查询商家订单（按商家名称）
    @Select("SELECT o.*, p.name as petName, p.species as petSpecies " +
            "FROM `order` o " +
            "LEFT JOIN pet p ON o.pet_id = p.id " +
            "WHERE o.shop_name = #{shopName} " +
            "ORDER BY o.created_at DESC")
    List<Order> findByShopName(String shopName);

    // 查询订单详情
    @Select("SELECT o.*, p.name as petName, p.species as petSpecies " +
            "FROM `order` o " +
            "LEFT JOIN pet p ON o.pet_id = p.id " +
            "WHERE o.id = #{id}")
    Order findById(Long id);

    // 更新订单状态
    @Update("UPDATE `order` SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}