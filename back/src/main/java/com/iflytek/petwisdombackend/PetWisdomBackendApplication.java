package com.iflytek.petwisdombackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.iflytek.petwisdombackend.mapper")
public class PetWisdomBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetWisdomBackendApplication.class, args);
    }
}
