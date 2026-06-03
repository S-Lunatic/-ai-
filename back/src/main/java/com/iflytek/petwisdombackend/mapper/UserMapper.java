package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(Long id);

    @Insert("INSERT INTO user(username, password, role, created_at) VALUES(#{username}, #{password}, #{role}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET role = #{role} WHERE id = #{userId}")
    int updateRole(@Param("userId") Long userId, @Param("role") String role);

    @Update("UPDATE user SET shop_name = #{shopName}, shop_address = #{shopAddress}, shop_phone = #{shopPhone} WHERE id = #{userId}")
    int updateShopInfo(@Param("userId") Long userId,
                       @Param("shopName") String shopName,
                       @Param("shopAddress") String shopAddress,
                       @Param("shopPhone") String shopPhone);
}