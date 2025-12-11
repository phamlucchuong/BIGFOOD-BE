package com.example.bigfood.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Review> findByOrder(Order order);
    Set<Review> findAllByOrder_Restaurant(Restaurant restaurant);
}
