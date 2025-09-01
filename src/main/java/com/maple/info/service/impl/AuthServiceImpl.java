package com.maple.info.service.impl;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.maple.info.service.AuthService;
import com.maple.info.service.UserService;
import com.maple.info.util.JwtTokenProvider;
import com.maple.info.vo.UserVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public String login(String id, String password) {
        UserVo user = userService.findById(id);
        if (user == null) throw new RuntimeException("존재하지 않는 사용자");
        if (user == null || !passwordEncoder.matches(password, user.getPw())) {
            throw new RuntimeException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // Access & Refresh Token 발급
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = UUID.randomUUID().toString();

        // DB에 Refresh Token 저장
        user.setRefreshToken(refreshToken);
        userService.updateRefreshToken(user);

        return accessToken + "," + refreshToken; 
    }

    @Override
    public String refreshToken(String refreshToken) {
        //String userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        // DB에 저장된 Refresh Token과 비교
    	UserVo user = userService.findByRefreshToken(refreshToken);
        if (user == null) throw new RuntimeException("유효하지 않은 Refresh Token");

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId());

        return newAccessToken;
    }
}