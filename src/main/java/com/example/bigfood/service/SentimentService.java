package com.example.bigfood.service;


import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.bigfood.dto.response.SentimentResponse;


@Service
public class SentimentService {

    private final WebClient webClient;

    public SentimentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8000").build();
    }

    public SentimentResponse analyzeReview(String reviewContent) {
        Map<String, String> body = Map.of("text", reviewContent);

        return webClient.post()
                .uri("/predict")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SentimentResponse.class)
                .block();

    }
}
