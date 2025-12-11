package com.renking.xmall.Config;

import com.renking.xmall.Common.Interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login/**",
                        "/product/list/**",
                        "/product/search/**",
                        "/product/{id}/**",
                        "/user/code/**",
                        "/error/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
