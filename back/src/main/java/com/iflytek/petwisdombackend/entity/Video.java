package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Video {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String videoUrl;
    private String category;
    private String status;  // pending, approved, rejected
    private String rejectReason;
    private Integer views;
    private Integer likes;
    private Date createdAt;
}