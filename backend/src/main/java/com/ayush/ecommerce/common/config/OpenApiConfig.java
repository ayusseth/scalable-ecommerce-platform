package com.ayush.ecommerce.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";
    @Bean
    public OpenAPI customOpenAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Platform API")
                        .version("1.0")
                        .description("Product-ready E-Commerce Backend built using Spring Boot")
                        .contact(new Contact()
                                .name("Ayush Seth")
                                .email("er.ayushseth@gmail.com"))
                ).addSecurityItem(new SecurityRequirement()
                        .addList(SECURITY_SCHEME_NAME))
                .schemaRequirement(SECURITY_SCHEME_NAME, securityScheme);
    }
}
