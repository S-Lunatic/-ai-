package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.PetShare;
import com.iflytek.petwisdombackend.mapper.PetShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PetShareService {

    @Autowired
    private PetShareMapper petShareMapper;

    public List<PetShare> getSharesByPetId(Long petId) {
        return petShareMapper.findByPetId(petId);
    }

    public boolean addShare(PetShare share) {
        // 计算过期时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, share.getExpireDays());
        share.setExpireAt(cal.getTime());
        share.setStatus("active");
        return petShareMapper.insert(share) > 0;
    }

    public boolean revokeShare(Long id) {
        return petShareMapper.revoke(id) > 0;
    }
}