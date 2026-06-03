package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Dispute;
import com.iflytek.petwisdombackend.mapper.DisputeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisputeService {

    @Autowired
    private DisputeMapper disputeMapper;

    public boolean createDispute(Dispute dispute) {
        return disputeMapper.insert(dispute) > 0;
    }

    public List<Dispute> getUserDisputes(Long userId) {
        return disputeMapper.findByUserId(userId);
    }

    public List<Dispute> getTargetDisputes(Long userId) {
        return disputeMapper.findByTargetId(userId);
    }

    public List<Dispute> getPendingDisputes() {
        return disputeMapper.findPending();
    }

    public List<Dispute> getResolvedDisputes() {
        return disputeMapper.findResolved();
    }

    public Dispute getDisputeById(Long id) {
        return disputeMapper.findById(id);
    }

    public boolean resolveDispute(Long id, String status, String result) {
        return disputeMapper.updateStatus(id, status, result) > 0;
    }
}