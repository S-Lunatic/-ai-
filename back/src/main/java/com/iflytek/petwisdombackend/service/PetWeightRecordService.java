package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.PetWeightRecord;
import com.iflytek.petwisdombackend.mapper.PetWeightRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetWeightRecordService {

    @Autowired
    private PetWeightRecordMapper weightRecordMapper;

    // 获取宠物体重记录列表
    public List<PetWeightRecord> getWeightList(Long petId) {
        return weightRecordMapper.findByPetId(petId);
    }

    // 获取近30天体重记录（图表用）
    public List<PetWeightRecord> getRecentWeightList(Long petId) {
        return weightRecordMapper.findRecentByPetId(petId);
    }

    // 获取最新体重
    public PetWeightRecord getLatestWeight(Long petId) {
        return weightRecordMapper.findLatestByPetId(petId);
    }

    // 添加体重记录
    public boolean addWeightRecord(PetWeightRecord record) {
        return weightRecordMapper.insert(record) > 0;
    }

    // 更新体重记录
    public boolean updateWeightRecord(PetWeightRecord record) {
        return weightRecordMapper.update(record) > 0;
    }

    // 删除体重记录
    public boolean deleteWeightRecord(Long id) {
        return weightRecordMapper.deleteById(id) > 0;
    }
}