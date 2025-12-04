package com.renking.xmall.Controller;

import com.renking.xmall.Entity.Dto.UserDto;
import com.renking.xmall.Entity.Result;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/login")
    public Result<UserDto> login(@RequestBody UserDto user) {
        UserDto userDto = userService.login(user.getPhone(), user.getPassword());
        return Result.success(userDto);
    }

}
