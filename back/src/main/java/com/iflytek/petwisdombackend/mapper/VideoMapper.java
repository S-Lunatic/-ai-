package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {

    @Insert("INSERT INTO video(user_id, title, description, video_url, category, status, created_at) " +
            "VALUES(#{userId}, #{title}, #{description}, #{videoUrl}, #{category}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Video video);

    @Select("SELECT * FROM video WHERE status = #{status} ORDER BY created_at DESC")
    List<Video> findByStatus(String status);

    @Update("UPDATE video SET status = #{status}, reject_reason = #{rejectReason} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("rejectReason") String rejectReason);
    // 查询用户自己的所有视频
    @Select("SELECT * FROM video WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Video> findByUserId(Long userId);

    @Update("UPDATE video SET category = #{category} WHERE id = #{id}")
    int updateCategory(@Param("id") Long id, @Param("category") String category);

}