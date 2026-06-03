package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Feedback {
    private Long id;
    private Long userId;
    private String content;
    private String category;
    private String status;
    private String reply;
    private Date createdAt;
}