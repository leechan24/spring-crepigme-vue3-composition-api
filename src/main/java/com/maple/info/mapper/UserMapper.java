package com.maple.info.mapper;

import com.maple.info.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
	UserVo findById(String id);
    void insertUser(UserVo user);
    void updateRefreshToken(UserVo user);
    UserVo getUserByRefreshToken(String refreshToken);
    List<UserVo> getAllUsers();
}
