package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String shopName;
    private String shopAddress;
    private String shopPhone;
    private Date createdAt;
}