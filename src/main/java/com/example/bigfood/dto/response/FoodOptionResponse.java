package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodOptionResponse {
    private String id;
    private String name;
    private double price;
    private boolean defaultPrice;
}
