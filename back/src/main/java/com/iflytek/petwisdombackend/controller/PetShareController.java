package com.iflytek.petwisdombackend.controller;

import com.iflytek.petwisdombackend.entity.PetShare;
import com.iflytek.petwisdombackend.service.PetShareService;
import com.iflytek.petwisdombackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pet-share")
public class PetShareController {

    @Autowired
    private PetShareService petShareService;

    @Autowired
    private UserService userService;

    // 获取宠物的授权列表
    @GetMapping("/list")
    public Map<String, Object> list(@RequestParam Long petId) {
        Map<String, Object> result = new HashMap<>();
        List<PetShare> list = petShareService.getSharesByPetId(petId);
        // 补充被授权人信息
        for (PetShare share : list) {
            var user = userService.findById(share.getSharedWithId());
            if (user != null) {
                if ("hospital".equals(share.getSharedWithType())) {
                    // 医院：显示店铺名称
                    String displayName = user.getShopName();
                    if (displayName == null || displayName.isEmpty()) {
                        displayName = user.getUsername();
                    }
                    share.setSharedWithName(displayName);
                } else {
                    share.setSharedWithName(user.getUsername());
                }
            }
        }
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 添加授权
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody PetShare share) {
        Map<String, Object> result = new HashMap<>();
        boolean success = petShareService.addShare(share);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "授权成功" : "授权失败");
        return result;
    }

    // 撤销授权
    @GetMapping("/revoke")
    public Map<String, Object> revoke(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        boolean success = petShareService.revokeShare(id);
        result.put("code", success ? 200 : 500);
        result.put("message", success ? "已撤销" : "操作失败");
        return result;
    }
}