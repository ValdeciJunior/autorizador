package com.vr.miniautorizador.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini-Autorizador API")
                        .version("1.0.0")
                        .description("API para criação de cartões, consultas de saldo e autorização de transações financeiras distribuídas sob alta concorrência.")
                        .contact(new Contact()
                                .name("Valdeci Rolim")
                                .email("rolim.valdeci.jr@gmail.com")));
    }
}
