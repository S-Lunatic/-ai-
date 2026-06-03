package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PetShare {
    private Long id;
    private Long petId;
    private Long sharedWithId;
    private String sharedWithType;
    private Integer expireDays;
    private Date expireAt;
    private String status;
    private Date createdAt;
    private String sharedWithName;
}