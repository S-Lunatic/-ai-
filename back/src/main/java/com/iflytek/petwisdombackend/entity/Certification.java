package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Certification {
    private Long id;
    private Long userId;
    private String type;
    private String status;
    private String rejectReason;
    private String realName;
    private String idCard;
    private String experience;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private String license;
    private Date createdAt;
}