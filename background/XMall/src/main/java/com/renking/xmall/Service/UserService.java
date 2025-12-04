package com.renking.xmall.Service;

import com.renking.xmall.Entity.Dto.UserDto;

public interface UserService {
    UserDto login(String phone, String password);
}
