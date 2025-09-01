package com.maple.info.vo;

import java.time.LocalDateTime;

import lombok.Data;

@Data // lombok으로 getter/setter 자동 생성 
public class UserVo {

    private String id;
    private String userName;
    private String pw;
    private String refreshToken; // Refresh Token 저장용
    private String role;
    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;
    

}
