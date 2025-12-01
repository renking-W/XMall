package com.renking.xmall.Utils;

import com.renking.xmall.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtil {

    // 生成token
    public String getToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", user.getPhone());
        claims.put("password", user.getPassword());
        return Jwts.builder()
                .setSubject(user.getPhone())
                .setClaims( claims)
                .compact();
    }

    // 解析 token
    public User parseToken(String token) {
        return null;
    }
}
