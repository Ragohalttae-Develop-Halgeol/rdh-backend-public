package com.sfermions.config;

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
        Info info = new Info()
                .title("Spring Boot REST API Specifications")
                .description("API 명세서입니다.")
                .version("1.0.0");

        // SecurityScheme 설정
        String jwtScheme = "jwtAuth";

        // API 요청 헤더에 인증정보 포함
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtScheme);

        // SecuritySchemes 등록
        Components components = new Components().addSecuritySchemes(jwtScheme,
                new SecurityScheme()
                        .name(jwtScheme)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // OpenAPI 객체 생성 및 Security 설정 추가
        return new OpenAPI()
                .components(components)  // 등록된 components 사용
                .info(info)
                .addSecurityItem(securityRequirement);
    }
}
