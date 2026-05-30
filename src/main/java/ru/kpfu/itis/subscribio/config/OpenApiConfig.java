package ru.kpfu.itis.subscribio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI subscribioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Subscribio API")
                        .description("REST API интернет-магазина цифровых подарочных карт и подписок")
                        .version("1.0"));
    }


}


