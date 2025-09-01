package com.maple.info.service;

import com.maple.info.vo.UserVo;
import java.util.List;

public interface UserService {
    UserVo findById(String id);
    void createUser(UserVo user);
    void updateRefreshToken(UserVo user);
    UserVo findByRefreshToken(String refreshToken);
    List<UserVo> getAllUsers();
}