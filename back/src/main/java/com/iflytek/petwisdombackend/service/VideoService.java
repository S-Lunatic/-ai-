package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Video;
import com.iflytek.petwisdombackend.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    public boolean save(Video video) {
        return videoMapper.insert(video) > 0;
    }

    public List<Video> findByStatus(String status) {
        return videoMapper.findByStatus(status);
    }

    public boolean audit(Long id, String status, String rejectReason) {
        return videoMapper.updateStatus(id, status, rejectReason) > 0;
    }

    public List<Video> findByUserId(Long userId) {
        return videoMapper.findByUserId(userId);
    }

    public boolean updateCategory(Long id, String category) {
        return videoMapper.updateCategory(id, category) > 0;
    }

}