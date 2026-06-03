package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Task;
import com.iflytek.petwisdombackend.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 获取待接单任务列表（任务大厅）
    @GetMapping("/pending")
    public Map<String, Object> getPendingTasks() {
        Map<String, Object> result = new HashMap<>();
        List<Task> list = taskService.getPendingTasks();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }
    //public Map<string,object> getpendingtasks(){
    // map<string,object> result = new HashMap<>();
    //}

    // 获取用户发布的任务
    @GetMapping("/my-published")
    public Map<String, Object> getMyPublishedTasks(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Task> list = taskService.getUserTasks(userId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取护工接单的任务（我的工作）
    @GetMapping("/my-accepted")
    public Map<String, Object> getMyAcceptedTasks(@RequestParam Long caregiverId) {
        Map<String, Object> result = new HashMap<>();
        List<Task> list = taskService.getCaregiverTasks(caregiverId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取任务详情
    @GetMapping("/detail")
    public Map<String, Object> getTaskDetail(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        Task task = taskService.getTaskById(id);
        if (task != null) {
            result.put("code", 200);
            result.put("data", task);
        } else {
            result.put("code", 404);
            result.put("message", "任务不存在");
        }
        return result;
    }

    // 发布任务
    @PostMapping("/publish")
    public Map<String, Object> publishTask(@RequestBody Task task) {
        Map<String, Object> result = new HashMap<>();
        boolean success = taskService.publishTask(task);
        if (success) {
            result.put("code", 200);
            result.put("message", "发布成功");
            result.put("data", task);
        } else {
            result.put("code", 500);
            result.put("message", "发布失败");
        }
        return result;
    }

    // 接单
    @PostMapping("/accept")
    public Map<String, Object> acceptTask(@RequestBody Map<String, Long> params) {
        Long taskId = params.get("taskId");
        Long caregiverId = params.get("caregiverId");

        Map<String, Object> result = new HashMap<>();
        boolean success = taskService.acceptTask(taskId, caregiverId);
        if (success) {
            result.put("code", 200);
            result.put("message", "接单成功");
        } else {
            result.put("code", 500);
            result.put("message", "接单失败，任务可能已被接走");
        }
        return result;
    }

    // 完成任务
    @PostMapping("/complete")
    public Map<String, Object> completeTask(@RequestBody Map<String, Long> params) {
        Long taskId = params.get("taskId");

        Map<String, Object> result = new HashMap<>();
        boolean success = taskService.completeTask(taskId);
        if (success) {
            result.put("code", 200);
            result.put("message", "任务已完成");
        } else {
            result.put("code", 500);
            result.put("message", "操作失败");
        }
        return result;
    }

    // 取消任务（仅发布者）
    @PostMapping("/cancel")
    public Map<String, Object> cancelTask(@RequestBody Map<String, Long> params) {
        Long taskId = params.get("taskId");
        Long userId = params.get("userId");

        Map<String, Object> result = new HashMap<>();
        boolean success = taskService.cancelTask(taskId, userId);
        if (success) {
            result.put("code", 200);
            result.put("message", "任务已取消");
        } else {
            result.put("code", 500);
            result.put("message", "取消失败，只能取消待接单状态的任务");
        }
        return result;
    }
}