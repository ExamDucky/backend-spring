package com.unihack.smart_usb.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"
)
public class OpenApiConfiguration {
    @Bean
    public GroupedOpenApi AuthAPI() {
        return GroupedOpenApi.builder()
                .displayName("AuthAPI")
                .group("Auth API")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi TestAPI() {
        return GroupedOpenApi.builder()
                .displayName("TestAPI")
                .group("Test API")
                .pathsToMatch("/test/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ExamAPI() {
        return GroupedOpenApi.builder()
                .displayName("ExamAPI")
                .group("Exam API")
                .pathsToMatch("/exam/**")
                .build();
    }
}
