package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.PetWeightRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PetWeightRecordMapper {

    // 查询宠物体重记录列表（按日期倒序）
    @Select("SELECT * FROM pet_weight_record WHERE pet_id = #{petId} ORDER BY record_date DESC")
    List<PetWeightRecord> findByPetId(Long petId);

    // 查询近30天体重记录（用于图表）
    @Select("SELECT * FROM pet_weight_record WHERE pet_id = #{petId} " +
            "AND record_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "ORDER BY record_date ASC")
    List<PetWeightRecord> findRecentByPetId(Long petId);

    // 查询最新体重记录
    @Select("SELECT * FROM pet_weight_record WHERE pet_id = #{petId} ORDER BY record_date DESC LIMIT 1")
    PetWeightRecord findLatestByPetId(Long petId);

    // 添加体重记录
    @Insert("INSERT INTO pet_weight_record(pet_id, weight, record_date, created_at) " +
            "VALUES(#{petId}, #{weight}, #{recordDate}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PetWeightRecord record);

    // 更新体重记录
    @Update("UPDATE pet_weight_record SET weight = #{weight}, record_date = #{recordDate} WHERE id = #{id}")
    int update(PetWeightRecord record);

    // 删除体重记录
    @Delete("DELETE FROM pet_weight_record WHERE id = #{id}")
    int deleteById(Long id);
}