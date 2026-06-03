package com.iflytek.petwisdombackend.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Pet {
    private Long id;
    private Long userId;
    private String name;
    private String species;
    private Integer age;
    private String chipNumber;
    private Date createdAt;
}