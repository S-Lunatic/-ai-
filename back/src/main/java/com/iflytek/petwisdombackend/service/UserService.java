package com.iflytek.petwisdombackend.service;

import com.iflytek.petwisdombackend.entity.User;
import com.iflytek.petwisdombackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean register(String username, String password) {
        User existUser = userMapper.findByUsername(username);
        if (existUser != null) {
            return false;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("petOwner");
        return userMapper.insert(user) > 0;
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public boolean updateUserRole(Long userId, String role) {
        return userMapper.updateRole(userId, role) > 0;
    }

    public boolean updateShopInfo(Long userId, String shopName, String shopAddress, String shopPhone) {
        return userMapper.updateShopInfo(userId, shopName, shopAddress, shopPhone) > 0;
    }
}