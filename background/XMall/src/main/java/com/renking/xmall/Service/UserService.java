package com.renking.xmall.Service;

import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.User;

import java.math.BigDecimal;

public interface UserService {
    UserDto login(String phone, String password);

    String getCode(String phone);

    UserDto register(UserDto user);

    UserDto getInfo(UserDto user);

    Boolean updateUserInfo(UserDto user);

    Boolean recharge(Integer id, BigDecimal mon);

    void reduceMoney(Integer userId, BigDecimal tar);
}
