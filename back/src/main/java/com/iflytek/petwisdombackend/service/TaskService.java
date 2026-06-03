package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Task;
import com.iflytek.petwisdombackend.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    // 获取待接单任务列表
    public List<Task> getPendingTasks() {
        return taskMapper.findPendingTasks();
    }

    // 获取用户发布的任务
    public List<Task> getUserTasks(Long userId) {
        return taskMapper.findByUserId(userId);
    }

    // 获取护工接单的任务
    public List<Task> getCaregiverTasks(Long caregiverId) {
        return taskMapper.findByAcceptedBy(caregiverId);
    }

    // 获取任务详情
    public Task getTaskById(Long id) {
        return taskMapper.findById(id);
    }

    // 发布任务
    public boolean publishTask(Task task) {
        return taskMapper.insert(task) > 0;
    }

    // 接单
    public boolean acceptTask(Long taskId, Long caregiverId) {
        return taskMapper.acceptTask(taskId, caregiverId) > 0;
    }

    // 完成任务
    public boolean completeTask(Long taskId) {
        return taskMapper.completeTask(taskId) > 0;
    }

    // 取消任务
    public boolean cancelTask(Long taskId, Long userId) {
        return taskMapper.cancelTask(taskId, userId) > 0;
    }
}