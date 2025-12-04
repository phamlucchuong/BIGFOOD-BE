package com.example.bigfood.configuration;

import org.springframework.context.annotation. Bean;
import org.springframework. context.annotation.Configuration;
import org.springframework.web.reactive. function.client.WebClient;

@Configuration
public class GoongWebClientConfig {
    
    @Bean
    public WebClient goongWebClient() {
        return WebClient.builder()
                .baseUrl("https://rsapi.goong.io")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}