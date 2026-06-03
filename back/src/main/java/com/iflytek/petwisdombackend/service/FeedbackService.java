package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Feedback;
import com.iflytek.petwisdombackend.mapper.FeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    public boolean submitFeedback(Feedback feedback) {
        return feedbackMapper.insert(feedback) > 0;
    }

    public List<Feedback> getAllFeedback() {
        return feedbackMapper.findAll();
    }

    public List<Feedback> getPendingFeedback() {
        return feedbackMapper.findPending();
    }

    public List<Feedback> getUserFeedback(Long userId) {
        return feedbackMapper.findByUserId(userId);
    }

    public boolean resolveFeedback(Long id, String category, String reply) {
        return feedbackMapper.update(id, category, reply) > 0;
    }
}