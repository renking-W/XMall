package com.renking.xmall.Mapper;

import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    User selectByPhoneAndPassword(@Param("phone") String phone,@Param("password") String password);

    int insertUserIfNotExist(UserDto userDto);

    UserDto selectByUserName(String userName);
}