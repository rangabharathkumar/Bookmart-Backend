package com.bookmart.bookmart_backend.confiiguration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:BookMart Backend}")
    private String appName;

    @Value("${app.swagger.prod-url:https://api.bookmart.com}")
    private String productionUrl;

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI myOpenAPI() {
        // Server Configuration
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development Server");

        Server prodServer = new Server();
        prodServer.setUrl(productionUrl); // Uses environment variable
        prodServer.setDescription("Production Server");

        // Contact Information
        Contact contact = new Contact();
        contact.setEmail("support@bookmart.com");
        contact.setName("BookMart Support");
        contact.setUrl("https://www.bookmart.com");

        // License
        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        // API Information
        Info info = new Info()
                .title("📚 BookMart API Documentation")
                .version("1.0.0")
                .contact(contact)
                .description("Complete REST API documentation for BookMart Backend - An online bookstore management system with JWT authentication, role-based access control, and comprehensive book and order management.")
                .termsOfService("https://www.bookmart.com/terms")
                .license(mitLicense);

        // Security Scheme - JWT Bearer Token
        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter JWT Bearer token in the format: Bearer {token}");

        // Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);

        // API Tags with Descriptions
        Tag authTag = new Tag()
                .name("Authentication")
                .description("User authentication and registration endpoints. Handles login, signup, and JWT token generation.");

        Tag bookTag = new Tag()
                .name("Book Management")
                .description("Book CRUD operations. Public endpoints for viewing books, admin-only endpoints for managing inventory.");

        Tag userTag = new Tag()
                .name("User Management")
                .description("User profile and account management endpoints.");

        Tag orderTag = new Tag()
                .name("Order Management")
                .description("Order placement, tracking, and status management for users and administrators.");

        Tag adminTag = new Tag()
                .name("Admin Operations")
                .description("Administrative endpoints for user management and role assignment. Requires ADMIN role.");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                .tags(List.of(authTag, bookTag, userTag, orderTag, adminTag));
    }
}