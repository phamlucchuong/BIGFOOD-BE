package com.example.bigfood.dto;

public interface FinanceProjection {
    double getMonthIncome();
    int getMonthDirection();
    double getDayIncome();
    int getDayDirection();
}