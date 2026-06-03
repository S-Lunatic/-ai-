package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Feedback;
import com.iflytek.petwisdombackend.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // 提交反馈
    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody Map<String, String> params) {
        Long userId = Long.valueOf(params.get("userId"));
        String content = params.get("content");

        Map<String, Object> result = new HashMap<>();

        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setContent(content);

        boolean success = feedbackService.submitFeedback(feedback);
        if (success) {
            result.put("code", 200);
            result.put("message", "反馈已提交");
        } else {
            result.put("code", 500);
            result.put("message", "提交失败");
        }
        return result;
    }

    // 获取所有反馈（管理员）
    @GetMapping("/all")
    public Map<String, Object> getAll() {
        Map<String, Object> result = new HashMap<>();
        List<Feedback> list = feedbackService.getAllFeedback();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取待处理反馈（管理员）
    @GetMapping("/pending")
    public Map<String, Object> getPending() {
        Map<String, Object> result = new HashMap<>();
        List<Feedback> list = feedbackService.getPendingFeedback();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 处理反馈（管理员）
    @PostMapping("/resolve")
    public Map<String, Object> resolve(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String category = params.get("category").toString();
        String reply = params.get("reply").toString();

        Map<String, Object> result = new HashMap<>();
        boolean success = feedbackService.resolveFeedback(id, category, reply);
        if (success) {
            result.put("code", 200);
            result.put("message", "处理成功");
        } else {
            result.put("code", 500);
            result.put("message", "处理失败");
        }
        return result;
    }
}