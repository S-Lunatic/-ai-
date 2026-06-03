package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Pet;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PetMapper {

    // 查询用户的宠物列表
    @Select("SELECT * FROM pet WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Pet> findByUserId(Long userId);

    // 根据ID查询宠物
    @Select("SELECT * FROM pet WHERE id = #{id}")
    Pet findById(Long id);

    // 添加宠物
    @Insert("INSERT INTO pet(user_id, name, species, age, chip_number, created_at) " +
            "VALUES(#{userId}, #{name}, #{species}, #{age}, #{chipNumber}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Pet pet);

    // 更新宠物
    @Update("UPDATE pet SET name = #{name}, species = #{species}, age = #{age}, " +
            "chip_number = #{chipNumber} WHERE id = #{id}")
    int update(Pet pet);

    // 删除宠物
    @Delete("DELETE FROM pet WHERE id = #{id}")
    int deleteById(Long id);
}