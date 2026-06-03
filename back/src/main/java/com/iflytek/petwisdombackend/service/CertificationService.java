package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.Certification;
import com.iflytek.petwisdombackend.mapper.CertificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationService {

    @Autowired
    private CertificationMapper certMapper;

    public boolean submitCertification(Certification cert) {
        return certMapper.insert(cert) > 0;
    }

    public Certification getUserCertification(Long userId, String type) {
        return certMapper.findByUserAndType(userId, type);
    }

    public List<Certification> getPendingCertifications() {
        return certMapper.findPending();
    }

    public Certification getCertificationById(Long id) {
        return certMapper.findById(id);
    }

    public boolean approveCertification(Long id) {
        return certMapper.updateStatus(id, "approved", null) > 0;
    }

    public boolean rejectCertification(Long id, String reason) {
        return certMapper.updateStatus(id, "rejected", reason) > 0;
    }
}