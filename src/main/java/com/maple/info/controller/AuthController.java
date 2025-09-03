package com.maple.info.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.maple.info.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        String userId = payload.get("id");
        String password = payload.get("password");

        // 서비스에서 로그인 검증 후 토큰 발급
        Map<String, Object> tokens = authService.login(userId, password);

        // Refresh Token을 HttpOnly 쿠키로 내려주기
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", (String) tokens.get("refreshToken"))
                .httpOnly(true)                     // JS에서 접근 불가
                .path("/")                          // 모든 경로에서 쿠키 전송
                .maxAge(7 * 24 * 60 * 60)           // 7일 유지
                //.maxAge(180) // test용 3분 유지 
                .secure(false)                      // HTTPS 환경이면 true
                .sameSite("Strict")                 // CSRF 방어
                .build();

        // 응답 바디에는 Access Token + 유저 정보만 내려주기 [이러면 비밀번호도 들어가서 나중에 수정해야함 id, 권한으로만] 
        Map<String, Object> body = new HashMap<>();
        body.put("accessToken", tokens.get("accessToken"));
        body.put("user", tokens.get("user")); 
        
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(body);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String userId) {
    	
        // DB에 저장된 Refresh Token 제거
        authService.logout(userId);

        // 클라이언트 쿠키도 삭제 (maxAge=0)
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .secure(false)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@CookieValue("refreshToken") String refreshToken) {
        // refreshToken 유효성 검증 및 새로운 Access Token 발급
        String newAccessToken = authService.refreshAccessToken(refreshToken);

        Map<String, String> body = new HashMap<>();
        body.put("accessToken", newAccessToken);

        return ResponseEntity.ok(body);
    }
}