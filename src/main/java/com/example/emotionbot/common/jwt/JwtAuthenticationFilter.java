package com.example.emotionbot.common.jwt;

import com.example.emotionbot.common.auth.BearerAuthExtractor;
import com.example.emotionbot.common.response.APIErrorResponse;
import com.example.emotionbot.common.utils.JwtAuthenticationProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/swagger-resources")
                || path.startsWith("/swagger-ui.html")
                || path.startsWith("/webjars/")
                || path.startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = BearerAuthExtractor.extract(request);
        try {
            if (!Objects.isNull(token)) {
                verifyTokenAndSetAuthentication(token);
            }
            filterChain.doFilter(request, response);
        } catch (ServletException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            APIErrorResponse responseMessage = new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            response.getWriter().write(convertObjectToJson(responseMessage));
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            APIErrorResponse responseMessage = new APIErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
            response.getWriter().write(convertObjectToJson(responseMessage));
        }
    }

    private void verifyTokenAndSetAuthentication(String token) {
        Authentication authentication = jwtAuthenticationProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
