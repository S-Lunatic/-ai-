package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Dispute {
    private Long id;
    private Long orderId;
    private Long taskId;
    private Long initiatorId;
    private Long targetId;
    private String reason;
    private String description;
    private String status;  // pending, processing, resolved
    private String result;
    private Date createdAt;
}