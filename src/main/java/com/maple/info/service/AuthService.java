package com.maple.info.service;

import java.util.Map;

public interface AuthService {
	Map<String, Object> login(String userId, String password);        // 로그인 + Access Token 발급
    String refreshAccessToken(String refreshToken);       // Refresh Token으로 Access Token 재발급
    void logout(String userId);
}