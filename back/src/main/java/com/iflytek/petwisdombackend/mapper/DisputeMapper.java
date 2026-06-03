package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Dispute;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DisputeMapper {

    @Insert("INSERT INTO dispute(order_id, task_id, initiator_id, target_id, reason, description, status) " +
            "VALUES(#{orderId}, #{taskId}, #{initiatorId}, #{targetId}, #{reason}, #{description}, 'pending')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Dispute dispute);

    @Select("SELECT * FROM dispute WHERE initiator_id = #{userId} ORDER BY created_at DESC")
    List<Dispute> findByUserId(Long userId);

    @Select("SELECT * FROM dispute WHERE target_id = #{userId} ORDER BY created_at DESC")
    List<Dispute> findByTargetId(Long userId);

    @Select("SELECT * FROM dispute WHERE status = 'pending' ORDER BY created_at DESC")
    List<Dispute> findPending();

    @Select("SELECT * FROM dispute WHERE status = 'resolved' ORDER BY created_at DESC")
    List<Dispute> findResolved();

    @Select("SELECT * FROM dispute WHERE id = #{id}")
    Dispute findById(Long id);

    @Update("UPDATE dispute SET status = #{status}, result = #{result} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("result") String result);
}