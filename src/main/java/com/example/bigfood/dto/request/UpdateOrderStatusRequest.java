package com.example.bigfood.dto.request;

import com.example.bigfood.enums.OrderStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
public class UpdateOrderStatusRequest {
    @Enumerated(EnumType.STRING)
    OrderStatus status;
    @Builder.Default
    String reason = "";
}
