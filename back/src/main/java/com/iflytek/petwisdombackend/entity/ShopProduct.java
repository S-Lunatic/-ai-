package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class ShopProduct {
    private Long id;
    private Long shopId;
    private String name;
    private Double price;
    private String unit;
    private String status;
    private Date createdAt;
}