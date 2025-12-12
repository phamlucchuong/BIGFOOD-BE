package com.example.bigfood.dto;

public interface RestaurantProjection {
    String getRestaurantId();
    String getRestaurantName();
    String getAddress();
    Double getDistanceM(); // alias trong SELECT
    String getBannerId();
    double getRating();
}