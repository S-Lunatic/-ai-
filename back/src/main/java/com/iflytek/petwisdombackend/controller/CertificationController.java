package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Certification;
import com.iflytek.petwisdombackend.service.CertificationService;
import com.iflytek.petwisdombackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certification")
public class CertificationController {

    @Autowired
    private CertificationService certService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public Map<String, Object> submit(@RequestBody Certification cert) {
        Map<String, Object> result = new HashMap<>();
        boolean success = certService.submitCertification(cert);
        if (success) {
            result.put("code", 200);
            result.put("message", "申请已提交");
        } else {
            result.put("code", 500);
            result.put("message", "提交失败");
        }
        return result;
    }

    @GetMapping("/pending")
    public Map<String, Object> getPending() {
        Map<String, Object> result = new HashMap<>();
        List<Certification> list = certService.getPendingCertifications();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/approve")
    public Map<String, Object> approve(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        Map<String, Object> result = new HashMap<>();
        Certification cert = certService.getCertificationById(id);
        if (cert == null) {
            result.put("code", 404);
            result.put("message", "申请不存在");
            return result;
        }

        boolean success = certService.approveCertification(id);
        if (success) {
            // 如果是商家申请，保存店铺信息到 user 表
            if ("merchant".equals(cert.getType())) {
                userService.updateShopInfo(cert.getUserId(),
                        cert.getShopName(),
                        cert.getShopAddress(),
                        cert.getShopPhone());
            }
            userService.updateUserRole(cert.getUserId(), cert.getType());
            result.put("code", 200);
            result.put("message", "审核通过");
        } else {
            result.put("code", 500);
            result.put("message", "操作失败");
        }
        return result;
    }

    @PostMapping("/reject")
    public Map<String, Object> reject(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String reason = params.get("reason").toString();

        Map<String, Object> result = new HashMap<>();
        boolean success = certService.rejectCertification(id, reason);
        if (success) {
            result.put("code", 200);
            result.put("message", "已驳回");
        } else {
            result.put("code", 500);
            result.put("message", "操作失败");
        }
        return result;
    }
}