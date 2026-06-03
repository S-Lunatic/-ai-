package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Task {
    private Long id;
    private Long userId;           // 发布者ID
    private String title;
    private String type;           // walking/feeding/playing
    private Double reward;
    private String location;
    private Long petId;
    private String requirement;
    private Date taskTime;         // 任务时间
    private String status;         // pending/processing/completed
    private Long acceptedBy;       // 接单护工ID
    private Date createdAt;
}