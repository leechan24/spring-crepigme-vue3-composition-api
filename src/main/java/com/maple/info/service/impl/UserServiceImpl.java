package com.maple.info.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.maple.info.mapper.UserMapper;
import com.maple.info.service.UserService;
import com.maple.info.vo.UserVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserVo findById(String id) {
        return userMapper.findById(id);
    }

    @Override
    public void createUser(UserVo user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userMapper.insertUser(user);
    }

    @Override
    public void updateRefreshToken(UserVo user) {
        userMapper.updateRefreshToken(user);
    }
    
    @Override
    public UserVo findByRefreshToken(String refreshToken) {
        return userMapper.findByRefreshToken(refreshToken);
    }

    @Override
    public List<UserVo> getAllUsers() {
        return userMapper.getAllUsers();
    }
}
