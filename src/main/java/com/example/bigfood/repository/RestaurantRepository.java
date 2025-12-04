package com.example.bigfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bigfood.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    
}
