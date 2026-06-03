package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Pet;
import com.iflytek.petwisdombackend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    // 获取用户的宠物列表
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Pet> list = petService.getPetList(userId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取宠物详情
    @GetMapping("/detail")
    public Map<String, Object> detail(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        Pet pet = petService.getPetById(id);
        if (pet != null) {
            result.put("code", 200);
            result.put("data", pet);
        } else {
            result.put("code", 404);
            result.put("message", "宠物不存在");
        }
        return result;
    }

    // 添加宠物
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody Pet pet) {
        Map<String, Object> result = new HashMap<>();
        boolean success = petService.addPet(pet);
        if (success) {
            result.put("code", 200);
            result.put("message", "添加成功");
            result.put("data", pet);
        } else {
            result.put("code", 500);
            result.put("message", "添加失败");
        }
        return result;
    }

    // 更新宠物
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody Pet pet) {
        Map<String, Object> result = new HashMap<>();
        boolean success = petService.updatePet(pet);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "更新失败");
        }
        return result;
    }

    // 删除宠物
    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = petService.deletePet(id);
        if (success) {
            result.put("code", 200);
            result.put("message", "删除成功");
        } else {
            result.put("code", 500);
            result.put("message", "删除失败");
        }
        return result;
    }
}