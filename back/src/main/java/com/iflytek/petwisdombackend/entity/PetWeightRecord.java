package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PetWeightRecord {
    private Long id;
    private Long petId;
    private Double weight;
    private Date recordDate;
    private Date createdAt;
}