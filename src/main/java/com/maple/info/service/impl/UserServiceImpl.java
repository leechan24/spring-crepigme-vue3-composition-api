package com.maple.info.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maple.info.mapper.UserMapper;
import com.maple.info.service.UserService;
import com.maple.info.vo.UserVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    //@Autowired
    //private RedisTemplate<String, String> redisTemplate;
    
    private static final String PREFIX = "PHONE_CODE:"; 
    private static final long EXPIRATION_TIME = 5 * 60; // 5분 TTL
    
    // id 인증
    @Override
    public UserVo findById(String id) {
        return userMapper.findById(id);
    }
    
    // 회원가입 
    @Override
    public void registerUser(UserVo user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userMapper.insertUser(user);
    }
    
    // 인증번호 발송
    public void sendVerificationCode(String phoneNumber) {
    	
        String code = String.valueOf((int)((Math.random() * 900000) + 100000));
        redisTemplate.opsForValue().set(PREFIX + phoneNumber, code, EXPIRATION_TIME, TimeUnit.SECONDS);
        // Naver SENS로 SMS 발송
        // sendSmsViaNaverSens(phoneNumber, code);
        System.out.println("발송된 인증번호: " + code);
    }

    // 인증번호 검증
    public boolean verifyCode(String phoneNumber, String code) {
        String key = PREFIX + phoneNumber;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(key); // 성공하면 삭제
            return true;
        }
        return false;
    }

    // Naver SENS SMS 전송 [비즈니스 계정만 사용가능해서 보류]
    private void sendSmsViaNaverSens(String to, String code) {
        String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/{serviceId}/messages";
        String serviceId = "YOUR_SERVICE_ID";
        String accessKey = "YOUR_ACCESS_KEY";
        String secretKey = "YOUR_SECRET_KEY";
        String from = "발신번호";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("type", "SMS");
        body.put("from", from);
        body.put("content", "[인증번호] " + code + " 입니다.");
        body.put("messages", new Map[] { new HashMap<String, String>() {{
            put("to", to);
        }} });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", accessKey);
        headers.set("X-NCP-APIGW-API-KEY", secretKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl.replace("{serviceId}", serviceId), request, String.class);
            System.out.println("SMS 발송 응답: " + response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
