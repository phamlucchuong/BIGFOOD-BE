package com.example.bigfood.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bigfood.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    boolean existsByUserId(String userId);

    Optional<Restaurant> findByUserId(String userId);
}
