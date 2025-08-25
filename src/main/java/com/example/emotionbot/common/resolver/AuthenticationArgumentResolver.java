package com.example.emotionbot.common.resolver;

import com.example.emotionbot.common.utils.AuthorizationExtractor;
import com.example.emotionbot.common.utils.JwtTokenUtilV2;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class AuthenticationArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenUtilV2 jwtTokenUtil;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberAnnotation = parameter.hasParameterAnnotation(MemberId.class);
        boolean hasMemberType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasMemberAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token= AuthorizationExtractor.extract(request);
        String id=jwtTokenUtil.getPayload(token);

        return Long.parseLong(id);


    }
}
