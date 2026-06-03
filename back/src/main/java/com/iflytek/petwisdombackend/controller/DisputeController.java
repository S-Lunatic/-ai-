package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Dispute;
import com.iflytek.petwisdombackend.service.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispute")
public class DisputeController {

    @Autowired
    private DisputeService disputeService;

    // 发起纠纷
    @PostMapping("/create")
    public Map<String, Object> createDispute(@RequestBody Dispute dispute) {
        Map<String, Object> result = new HashMap<>();
        boolean success = disputeService.createDispute(dispute);
        if (success) {
            result.put("code", 200);
            result.put("message", "纠纷已提交");
        } else {
            result.put("code", 500);
            result.put("message", "提交失败");
        }
        return result;
    }

    // 获取用户发起的纠纷
    @GetMapping("/my-initiated")
    public Map<String, Object> getMyInitiated(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Dispute> list = disputeService.getUserDisputes(userId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取待处理纠纷（管理员）
    @GetMapping("/pending")
    public Map<String, Object> getPending() {
        Map<String, Object> result = new HashMap<>();
        List<Dispute> list = disputeService.getPendingDisputes();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取已完结纠纷（管理员）
    @GetMapping("/resolved")
    public Map<String, Object> getResolved() {
        Map<String, Object> result = new HashMap<>();
        List<Dispute> list = disputeService.getResolvedDisputes();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 处理纠纷（管理员）
    @PostMapping("/resolve")
    public Map<String, Object> resolveDispute(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();
        String resultText = params.get("result").toString();

        Map<String, Object> result = new HashMap<>();
        boolean success = disputeService.resolveDispute(id, status, resultText);
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