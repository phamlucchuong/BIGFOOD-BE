package com.example.bigfood.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    int rating;
    String reviewText;
    LocalDateTime lastUpdateAt;
    String replyText;
    LocalDateTime replyAt;
    OrderResponse order;
}