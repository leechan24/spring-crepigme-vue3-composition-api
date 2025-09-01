package com.maple.info.controller;

import org.springframework.web.bind.annotation.*;

import com.maple.info.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String id, @RequestParam String password) {
        return authService.login(id, password); // access, refresh token 반환
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
}
