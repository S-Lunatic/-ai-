package com.iflytek.petwisdombackend.mapper;

import com.iflytek.petwisdombackend.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {

    // 查询待接单任务列表
    @Select("SELECT * FROM task WHERE status = 'pending' ORDER BY created_at DESC")
    List<Task> findPendingTasks();

    // 查询用户发布的任务
    @Select("SELECT * FROM task WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Task> findByUserId(Long userId);


    // 查询护工接单的任务
    @Select("SELECT * FROM task WHERE accepted_by = #{caregiverId} ORDER BY created_at DESC")
    List<Task> findByAcceptedBy(Long caregiverId);

    // 查询任务详情
    @Select("SELECT * FROM task WHERE id = #{id}")
    Task findById(Long id);

    // 发布任务
    @Insert("INSERT INTO task(user_id, title, type, reward, location, pet_id, requirement, task_time, status, created_at) " +
            "VALUES(#{userId}, #{title}, #{type}, #{reward}, #{location}, #{petId}, #{requirement}, #{taskTime}, 'pending', NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Task task);

    // 接单
    @Update("UPDATE task SET status = 'processing', accepted_by = #{caregiverId} WHERE id = #{id} AND status = 'pending'")
    int acceptTask(@Param("id") Long id, @Param("caregiverId") Long caregiverId);

    // 完成任务
    @Update("UPDATE task SET status = 'completed' WHERE id = #{id} AND status = 'processing'")
    int completeTask(Long id);

    // 取消任务（仅发布者）
    @Delete("DELETE FROM task WHERE id = #{id} AND user_id = #{userId} AND status = 'pending'")
    int cancelTask(@Param("id") Long id, @Param("userId") Long userId);
}