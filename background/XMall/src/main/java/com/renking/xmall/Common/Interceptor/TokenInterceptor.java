package com.renking.xmall.Common.Interceptor;

import com.renking.xmall.Common.exception.ServiceException;
import com.renking.xmall.Config.StatusCode;
import com.renking.xmall.Utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 对于需要token的接口进行验证
        String token = request.getHeader("Token");
        if (TokenUtil.isTokenExpired(token)) {
            throw new ServiceException("Token 无效或已经过期", StatusCode.TOKEN_INVALID);
        }
        return true;
    }
}
