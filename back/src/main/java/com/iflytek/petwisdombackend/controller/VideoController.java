package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.Video;
import com.iflytek.petwisdombackend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Value("${file.upload.path}")
    private String uploadPath;

    // 上传视频文件
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            result.put("code", 400);
            result.put("message", "请上传视频文件");
            return result;
        }

        // 检查文件大小（100MB）
        if (file.getSize() > 100 * 1024 * 1024) {
            result.put("code", 400);
            result.put("message", "视频文件不能超过100MB");
            return result;
        }

        try {
            // 生成唯一文件名
            String originalName = file.getOriginalFilename();
            String ext = originalName.substring(originalName.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + ext;

            // 创建目录
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 保存文件
            File dest = new File(uploadPath + fileName);
            file.transferTo(dest);

            // 返回访问URL
            String videoUrl = "/uploads/" + fileName;
            result.put("code", 200);
            result.put("data", videoUrl);
            result.put("message", "上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
        }
        return result;
    }

    // 发布视频
    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody Video video) {
        Map<String, Object> result = new HashMap<>();
        video.setStatus("pending");
        boolean success = videoService.save(video);
        if (success) {
            result.put("code", 200);
            result.put("message", "视频已提交，等待审核");
        } else {
            result.put("code", 500);
            result.put("message", "发布失败");
        }
        return result;
    }

    // 获取待审核视频
    @GetMapping("/pending")
    public Map<String, Object> getPending() {
        Map<String, Object> result = new HashMap<>();
        List<Video> list = videoService.findByStatus("pending");
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 获取已通过视频
    @GetMapping("/approved")
    public Map<String, Object> getApproved() {
        Map<String, Object> result = new HashMap<>();
        List<Video> list = videoService.findByStatus("approved");
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 审核视频
    @PostMapping("/audit")
    public Map<String, Object> audit(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        String status = params.get("status").toString();
        String rejectReason = params.get("rejectReason") != null ? params.get("rejectReason").toString() : null;

        Map<String, Object> result = new HashMap<>();
        boolean success = videoService.audit(id, status, rejectReason);
        if (success) {
            result.put("code", 200);
            result.put("message", status.equals("approved") ? "审核通过" : "已驳回");
        } else {
            result.put("code", 500);
            result.put("message", "操作失败");
        }
        return result;
    }

    // 获取用户自己的所有视频
    @GetMapping("/my-videos")
    public Map<String, Object> getMyVideos(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Video> list = videoService.findByUserId(userId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    @PostMapping("/update-category")
    public Map<String, Object> updateCategory(@RequestParam Long id, @RequestParam String category) {
        Map<String, Object> result = new HashMap<>();
        boolean success = videoService.updateCategory(id, category);
        result.put("code", success ? 200 : 500);
        return result;
    }
}