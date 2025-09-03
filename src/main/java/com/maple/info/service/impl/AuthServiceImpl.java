package com.maple.info.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.maple.info.mapper.UserMapper;
import com.maple.info.service.AuthService;
import com.maple.info.util.JwtTokenProvider;
import com.maple.info.vo.UserVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    //private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> login(String userId, String password) {
        UserVo user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // Access Token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userId);

        // Refresh Token 생성 (UUID)
        String refreshToken = UUID.randomUUID().toString();

        // DB에 Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userMapper.updateRefreshToken(user);

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("user", user);
        
        return tokens;
    }


    @Override
    public void logout(String userId) {
        // DB에서 Refresh Token 제거
    	UserVo user = new UserVo();
        user.setUserId(userId);
        user.setRefreshToken(null);
        userMapper.updateRefreshToken(user);
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // DB에서 Refresh Token으로 사용자 검색
        UserVo user = userMapper.findByRefreshToken(refreshToken);
        if (user == null) {
            throw new RuntimeException("유효하지 않은 Refresh Token입니다.");
        }

        // 새로운 Access Token 발급
        return jwtTokenProvider.generateAccessToken(user.getUserId());
    }
}