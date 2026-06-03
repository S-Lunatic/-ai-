package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Certification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CertificationMapper {

    @Insert("INSERT INTO certification(user_id, type, status, real_name, id_card, experience, shop_name, shop_address, shop_phone, license, created_at) " +
            "VALUES(#{userId}, #{type}, 'pending', #{realName}, #{idCard}, #{experience}, #{shopName}, #{shopAddress}, #{shopPhone}, #{license}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Certification cert);

    @Select("SELECT * FROM certification WHERE user_id = #{userId} AND type = #{type}")
    Certification findByUserAndType(@Param("userId") Long userId, @Param("type") String type);

    @Select("SELECT * FROM certification WHERE status = 'pending' ORDER BY created_at DESC")
    List<Certification> findPending();

    @Select("SELECT * FROM certification WHERE id = #{id}")
    Certification findById(Long id);

    @Update("UPDATE certification SET status = #{status}, reject_reason = #{rejectReason} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("rejectReason") String rejectReason);
}