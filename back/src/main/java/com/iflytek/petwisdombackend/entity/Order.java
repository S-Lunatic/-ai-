package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Order {
    private Long id;
    private String orderNo;
    private Long userId;
    private String shopName;
    private String serviceName;
    private Long petId;
    private Date appointTime;
    private String remark;
    private String status;  // pending, processing, completed, cancelled
    private Integer rating;
    private Date createdAt;
    private String petName;
    private String petSpecies;
}