package com.example.emotionbot.common.jwt;

import com.example.emotionbot.common.utils.AuthorizationExtractor;
import com.example.emotionbot.common.utils.JwtTokenUtilV2;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {
    private final JwtTokenUtilV2 jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                            Object handler){
        final String token= AuthorizationExtractor.extract(request);
        jwtTokenUtil.verify(token);
        return true;
    }
}
