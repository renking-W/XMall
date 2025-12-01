package com.renking.xmall.Controller;

import com.renking.xmall.Entity.Response.LoginResponse;
import com.renking.xmall.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(String phone, String password) {
        userService.login(phone,password);
        return null;
    }

}
