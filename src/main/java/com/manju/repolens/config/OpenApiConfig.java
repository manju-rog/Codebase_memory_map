package com.manju.repolens.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI repoLensOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("RepoLens AI API")
                .description("AI-powered Codebase Memory Map for Spring Boot repositories")
                .version("1.0.0"));
    }
}
