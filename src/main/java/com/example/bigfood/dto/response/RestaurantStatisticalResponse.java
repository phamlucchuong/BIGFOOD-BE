package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RestaurantStatisticalResponse {
    
    double totalPrice;
    double percentagePrice;
    double averageUnitRevenuePrice;

    int numberOfOrder;
    double percentageOrder;
    int numberOrderCompleted;
    int numberOrderRejected;

    double averageStars;
    double percentageStart;
    double percentagePositive;
    double percentNegative;
}
