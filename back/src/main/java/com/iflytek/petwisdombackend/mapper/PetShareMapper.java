package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.PetShare;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PetShareMapper {

    @Select("SELECT * FROM pet_share WHERE pet_id = #{petId} AND status = 'active'")
    List<PetShare> findByPetId(Long petId);

    @Insert("INSERT INTO pet_share(pet_id, shared_with_id, shared_with_type, expire_days, expire_at, created_at) " +
            "VALUES(#{petId}, #{sharedWithId}, #{sharedWithType}, #{expireDays}, #{expireAt}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PetShare share);

    @Update("UPDATE pet_share SET status = 'revoked' WHERE id = #{id}")
    int revoke(Long id);
}