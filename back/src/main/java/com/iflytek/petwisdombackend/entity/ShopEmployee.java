package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ShopEmployee {
    private Long id;
    private Long shopId;
    private String name;
    private String position;
    private String icon;
    private Date createdAt;
}