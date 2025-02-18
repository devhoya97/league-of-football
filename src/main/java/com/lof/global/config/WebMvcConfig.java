package com.lof.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lof.auth.implement.TokenParser;
import com.lof.global.interceptor.AuthInterceptor;
import com.lof.global.interceptor.LogInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final TokenParser tokenParser;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인터셉터에서 memberId를 로깅하고 싶어서 순서가 2번째인데 많이 부자연스럽나?
        registry.addInterceptor(new AuthInterceptor(tokenParser))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/signup");
        registry.addInterceptor(new LogInterceptor())
                .order(2)
                .addPathPatterns("/**");
    }
}
