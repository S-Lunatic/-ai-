package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Pet;
import com.iflytek.petwisdombackend.mapper.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetMapper petMapper;

    // 获取用户的宠物列表
    public List<Pet> getPetList(Long userId) {
        return petMapper.findByUserId(userId);
    }

    // 获取宠物详情
    public Pet getPetById(Long id) {
        return petMapper.findById(id);
    }

    // 添加宠物
    public boolean addPet(Pet pet) {
        return petMapper.insert(pet) > 0;
    }

    // 更新宠物
    public boolean updatePet(Pet pet) {
        return petMapper.update(pet) > 0;
    }

    // 删除宠物
    public boolean deletePet(Long id) {
        return petMapper.deleteById(id) > 0;
    }
}