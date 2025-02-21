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
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**");
        registry.addInterceptor(new AuthInterceptor(tokenParser))
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/login", "/signup");
    }
}
