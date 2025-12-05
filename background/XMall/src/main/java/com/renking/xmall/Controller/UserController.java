package com.renking.xmall.Controller;

import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 密码登录，需要已有账号
     * @param user
     * @return
     */
    @PostMapping("/login/password")
    public Result<UserDto> login(@RequestBody UserDto user) {
        UserDto userDto = userService.login(user.getPhone(), user.getPassword());
        if(userDto == null) return Result.failure(StatusCode.USER_NOT_EXIST, "用户不存在或密码错误");
        return Result.success(userDto,"登录成功");
    }

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    @GetMapping("/code/{phone}")
    public Result<String> getCode(@PathVariable String phone) {
        userService.getCode(phone);
        return Result.success("获取验证码成功");
    }

    /**
     * 验证码登录，自动创建新账号
     * @param user
     * @return
     */
    @PostMapping("/login/code")
    public Result<UserDto> register(@RequestBody UserDto user) {
        UserDto userDto = userService.register(user);
        return Result.success(userDto,"登录成功");
    }

    /**
     * 获取用户信息
     * @param userName
     * @return
     */
    @PostMapping("/info")
    public Result<UserDto> getInfoByUserName(@RequestBody String userName) {
        UserDto userDto = userService.getInfo(userName);
        return Result.success(userDto,"获取用户信息成功");
    }

}
