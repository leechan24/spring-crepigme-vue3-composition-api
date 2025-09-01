package com.maple.info.service;

public interface AuthService {
    String login(String id, String password);        // 로그인 + Access Token 발급
    String refreshToken(String refreshToken);       // Refresh Token으로 Access Token 재발급
}