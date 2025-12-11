package com.renking.xmall.Utils;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Config.TokenConfig;
import com.renking.xmall.Entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j

@Component
public class TokenUtil {

    private static Key getSigningKey() {
        return new SecretKeySpec(TokenConfig.TOKEN_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 生成token
     * @param user
     * @return  token
     */
    public static String getToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", user.getPhone());
        claims.put("password", user.getPassword());
        return Jwts.builder()
                .setSubject(user.getPhone())
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+TokenConfig.TOKEN_EXPIRE_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析token
     * @param token
     * @return  claims
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .setAllowedClockSkewSeconds(30)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("{}:Token 已过期",token,e);
            throw new ServiceException("Token 无效或已经过期", StatusCode.TOKEN_INVALID);
        } catch (SignatureException e) {
            throw new ServiceException("Token 签名验证失败", StatusCode.TOKEN_INVALID);
        } catch (MalformedJwtException e) {
            throw new ServiceException("Token 格式错误", StatusCode.TOKEN_INVALID);
        } catch (Exception e) {
            throw new ServiceException("Token 解析失败", StatusCode.TOKEN_INVALID);
        }
    }

    public static Boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

}
