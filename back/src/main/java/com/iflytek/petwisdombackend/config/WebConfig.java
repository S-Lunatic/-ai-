package com.iflytek.petwisdombackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 支持 D 盘上传的视频
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath)
                .addResourceLocations("classpath:/static/uploads/videos/");
    }
}