package com.renking.xmall.Service.Impl;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.User;
import com.renking.xmall.Mapper.UserMapper;
import com.renking.xmall.Service.UserService;
import com.renking.xmall.Utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    @Override
    public UserDto login(String phone, String password) {
        User user = userMapper.selectByPhoneAndPassword(phone, password);
        //用户不存在抛出异常
        if(user==null) throw new ServiceException("用户不存在或密码错误！", StatusCode.USER_NOT_EXIST);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user,userDto);
        //生成token
        String token = TokenUtil.getToken(user);
        userDto.setToken(token);
        return userDto;
    }
}
