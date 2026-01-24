package com.example.bigfood.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Review> findByOrder(Order order);
    Set<Review> findAllByOrder_Restaurant(Restaurant restaurant);

    @Query("""
            SELECT r FROM Review r
            WHERE r.order.restaurant = :restaurant
            AND (
                :filter IS NULL
                OR :filter = 'all'
                OR (:filter = 'not_replied' AND r.replyAt IS NULL)
                OR (:filter = 'replied' AND r.replyAt IS NOT NULL)
            )
            """)
    Set<Review> findAllByOrder_RestaurantAndSortReviews(Restaurant restaurant, String filter);

    @Query("""
    SELECT u.email
    FROM Review r
    JOIN r.order o
    JOIN o.restaurant res
    JOIN res.user u
    GROUP BY res.userId, u.email
    HAVING COUNT(r.id) > 10 AND AVG(r.rating) < 1
    """)
    List<String> getLowRatingEmail();
}
