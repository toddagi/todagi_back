package com.example.emotionbot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT";

        // 보안 설정
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        SecurityScheme securityScheme = new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // 최종 OpenAPI 객체 반환
        return new OpenAPI()
                .components(new Components().addSecuritySchemes(jwt, securityScheme))
                .info(apiInfo())
                .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
                .title("emotionBot API 명세서")
                .description("emotionBot 서비스 API 명세서")
                .version("1.0.0");
    }
}
