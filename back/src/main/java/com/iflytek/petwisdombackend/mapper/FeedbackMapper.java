package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Feedback;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FeedbackMapper {

    @Insert("INSERT INTO feedback(user_id, content, status, created_at) " +
            "VALUES(#{userId}, #{content}, 'pending', NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Feedback feedback);

    @Select("SELECT f.*, u.username as userName FROM feedback f " +
            "LEFT JOIN user u ON f.user_id = u.id " +
            "ORDER BY f.created_at DESC")
    List<Feedback> findAll();

    @Select("SELECT f.*, u.username as userName FROM feedback f " +
            "LEFT JOIN user u ON f.user_id = u.id " +
            "WHERE f.status = 'pending' ORDER BY f.created_at DESC")
    List<Feedback> findPending();

    @Select("SELECT * FROM feedback WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Feedback> findByUserId(Long userId);

    @Update("UPDATE feedback SET category = #{category}, status = 'resolved', reply = #{reply} WHERE id = #{id}")
    int update(@Param("id") Long id, @Param("category") String category, @Param("reply") String reply);
}