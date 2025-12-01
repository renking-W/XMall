package com.renking.xmall.Service.Impl;

import com.renking.xmall.Entity.Response.LoginResponse;
import com.renking.xmall.Entity.User;
import com.renking.xmall.Mapper.UserMapper;
import com.renking.xmall.Service.UserService;
import com.renking.xmall.Utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final TokenUtil tokenUtil;
    @Override
    public LoginResponse login(String phone, String password) {
        User user = userMapper.selectByPhoneAndPassword(phone, password);
        if(user==null) {
            return LoginResponse.failure("用户不存在或密码错误！");
        }
        return LoginResponse.success("登录成功！",tokenUtil.getToken(user));
    }
}
