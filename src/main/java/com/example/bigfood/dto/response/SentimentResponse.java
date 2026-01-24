package com.example.bigfood.dto.response;
import com.example.bigfood.enums.Sentiment;

import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SentimentResponse {
    private String original_text;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private Sentiment sentiment;
}
