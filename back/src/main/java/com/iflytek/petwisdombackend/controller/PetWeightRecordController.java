package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.PetWeightRecord;
import com.iflytek.petwisdombackend.service.PetWeightRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weight")
public class PetWeightRecordController {

    @Autowired
    private PetWeightRecordService weightRecordService;

    // 获取宠物体重记录列表
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long petId) {
        Map<String, Object> result = new HashMap<>();
        List<PetWeightRecord> list = weightRecordService.getWeightList(petId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取近30天体重记录（图表用）
    @GetMapping("/recent")
    public Map<String, Object> recent(@RequestParam Long petId) {
        Map<String, Object> result = new HashMap<>();
        List<PetWeightRecord> list = weightRecordService.getRecentWeightList(petId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取最新体重
    @GetMapping("/latest")
    public Map<String, Object> latest(@RequestParam Long petId) {
        Map<String, Object> result = new HashMap<>();
        PetWeightRecord record = weightRecordService.getLatestWeight(petId);
        result.put("code", 200);
        if (record != null) {
            result.put("data", record);
        } else {
            result.put("data", null);
            result.put("message", "暂无体重记录");
        }
        return result;
    }

    // 添加体重记录
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody PetWeightRecord record) {
        Map<String, Object> result = new HashMap<>();
        boolean success = weightRecordService.addWeightRecord(record);
        if (success) {
            result.put("code", 200);
            result.put("message", "添加成功");
            result.put("data", record);
        } else {
            result.put("code", 500);
            result.put("message", "添加失败");
        }
        return result;
    }

    // 更新体重记录
    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody PetWeightRecord record) {
        Map<String, Object> result = new HashMap<>();
        boolean success = weightRecordService.updateWeightRecord(record);
        if (success) {
            result.put("code", 200);
            result.put("message", "更新成功");
        } else {
            result.put("code", 500);
            result.put("message", "更新失败");
        }
        return result;
    }

    // 删除体重记录
    @GetMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = weightRecordService.deleteWeightRecord(id);
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