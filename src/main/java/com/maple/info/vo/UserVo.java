package com.maple.info.vo;

import java.sql.Timestamp;

import lombok.Data;

@Data // lombok으로 getter/setter 자동 생성 
public class UserVo {

	private String userId;       // USER_ID
    private String username;     
    private String passwordHash; // PASSWORD_HASH
    private String email;
    private String status;
    private String refreshToken; // Refresh Token 저장용
    private String role;
    private String phoneNumber;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    private String accessToken; // Refresh Token 저장용

}
