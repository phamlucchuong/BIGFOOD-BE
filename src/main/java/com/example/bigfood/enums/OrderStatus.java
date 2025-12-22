package com.example.bigfood.enums;

import lombok.Getter;


@Getter
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    PREPARING,
    DELIVERING,
    COMPLETED,
    CANCELLED,
    REJECTED
}
