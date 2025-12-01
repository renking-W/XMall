package com.renking.xmall.Service;

import com.renking.xmall.Entity.Response.LoginResponse;
import com.renking.xmall.Entity.User;

public interface UserService {
    LoginResponse login(String phone, String password);
}
