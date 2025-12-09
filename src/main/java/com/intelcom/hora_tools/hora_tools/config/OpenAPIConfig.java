package com.intelcom.hora_tools.hora_tools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Horasphere Tools API")
                        .version("1.0.0")
                        .description("Backend tools API for Horasphere developers")
                        .contact(new Contact()
                                .name("Horasphere Team")
                                .email("support@horasphere.com")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Development server"));
    }
}
