package com.example.bigfood.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceResponse {
    @Builder.Default
    private double monthlyTarget = 50000000; // Example fixed target
    private double dayIncome;
    private int dayDirection; // true: increase, false: decrease
    private double monthIncome;
    private int monthDirection; // true: increase, false: decrease
}
