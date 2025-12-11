package com.renking.xmall.Controller;

import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Entity.Dto.OrderDto;
import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Entity.User;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 密码登录，需要已有账号
     * 前端传参： phone, password
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
     * 前端传参： phone
     * @param phone
     * @return
     */
    @GetMapping("/code/{phone}")
    public Result<String> getCode(@PathVariable String phone) {
        String code = userService.getCode(phone);
        return Result.success("获取验证码成功:"+code);
    }

    /**
     * 验证码登录，自动创建新账号
     * 前端传参： phone, code
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
     * 前端传参: id,userName
     * @param user
     * @return
     */
    @PostMapping("/info")
    public Result<UserDto> getInfoByUserName(@RequestBody UserDto user) {
        UserDto userDto = userService.getInfo(user);
        return Result.success(userDto,"获取用户信息成功");
    }

    /**
     * 修改用户信息
     * 前端传参： userName,phone,password
     * @param user
     * @return
     */
    @PostMapping("/update")
    public Result<Boolean> updateUserInfo(@RequestBody UserDto user) {
        Boolean flag = userService.updateUserInfo(user);
        if(flag)  return Result.success(flag,"修改用户信息成功");
        else return Result.failure(StatusCode.USER_NOT_EXIST, "用户不存在");
    }

    /**
     *  充值余额
     *  前端传参： id,mon(直接传递不需要包装对象)
     */
    @PutMapping("/recharge/{id}")
    public Result<Boolean> recharge(@PathVariable("id") Integer id ,@RequestBody BigDecimal mon) {
        Boolean flag = userService.recharge(id,mon);
        if(flag)  return Result.success(flag,"充值成功");
        else return Result.failure(StatusCode.USER_NOT_EXIST, "充值失败");
    }
}
