package com.example.bigfood.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.bigfood.dto.RestaurantProjection;
import com.example.bigfood.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

  boolean existsByUserId(String userId);

  Optional<Restaurant> findByUserId(String userId);

  @Query(value = """
      SELECT r.user_id as restaurantId, r.restaurant_name, r.address, r.banner_id,
             ST_Distance_Sphere(
                 r.location,
                 ST_SRID(POINT(:lng, :lat), 4326)
             ) AS distance_m,
             COALESCE(ratings.avg_rating, 0) AS rating,
             COALESCE(ratings.review_count, 0) AS review_count
      FROM restaurants r
      LEFT JOIN (
          SELECT o.restaurant_id, COALESCE(AVG(rev.rating), 0) as avg_rating, COUNT(rev.id) as review_count
          FROM orders o
          LEFT JOIN reviews rev ON o.id = rev.order_id
          GROUP BY o.restaurant_id
      ) ratings ON r.user_id = ratings.restaurant_id
      WHERE ST_Distance_Sphere(
              r.location,
              ST_SRID(POINT(:lng, :lat), 4326)
            ) <= :radius
      ORDER BY distance_m ASC
      """, countQuery = """
      SELECT COUNT(*)
      FROM restaurants r
      WHERE ST_Distance_Sphere(
              r.location,
              ST_SRID(POINT(:lng, :lat), 4326)
            ) <= :radius
      """, nativeQuery = true)
  Page<RestaurantProjection> findNearby(
      @Param("lat") double lat,
      @Param("lng") double lng,
      @Param("radius") double radiusMeters,
      Pageable pageable);

  @Query(value = """
      SELECT r.user_id AS restaurantId,
             r.restaurant_name AS restaurantName,
             r.address AS address,
             r.banner_id AS bannerId,
             COALESCE(AVG(rev.rating), 0) AS rating
      FROM (
          SELECT DISTINCT r.user_id, r.restaurant_name, r.address, r.banner_id
          FROM restaurants r
          JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
          WHERE rhc.restaurant_category_id = :categoryId
      ) r
      LEFT JOIN orders o ON r.user_id = o.restaurant_id
      LEFT JOIN reviews rev ON o.id = rev.order_id
      GROUP BY r.user_id, r.restaurant_name, r.address, r.banner_id
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      WHERE rhc.restaurant_category_id = :categoryId
      """, nativeQuery = true)
  Page<RestaurantProjection> findByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);

  @Query(value = """
      SELECT DISTINCT r.user_id AS restaurantId, r.restaurant_name AS restaurantName,
             r.address AS address, r.banner_id AS bannerId,
             ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) AS distance_m,
             COALESCE(ratings.avg_rating, 0) AS rating,
             COALESCE(ratings.review_count, 0) AS review_count
      FROM restaurants r
      LEFT JOIN (
          SELECT o.restaurant_id, COALESCE(AVG(rev.rating), 0) as avg_rating, COUNT(rev.id) as review_count
          FROM orders o
          LEFT JOIN reviews rev ON o.id = rev.order_id
          GROUP BY o.restaurant_id
      ) ratings ON r.user_id = ratings.restaurant_id

      WHERE r.is_approved = true
      AND ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) <= :radius
        AND (:categoryId IS NULL OR EXISTS (
          SELECT 1 FROM restaurant_has_categories rhc
          WHERE rhc.restaurant_id = r.user_id
          AND rhc.restaurant_category_id = :categoryId
        ))
        AND (:searchText IS NULL OR r.restaurant_name LIKE :searchText)

      ORDER BY distance_m ASC
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      WHERE r.is_approved = true
      AND ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) <= :radius
        AND (:categoryId IS NULL OR EXISTS (
          SELECT 1 FROM restaurant_has_categories rhc
          WHERE rhc.restaurant_id = r.user_id
          AND rhc.restaurant_category_id = :categoryId
        ))
        AND (:searchText IS NULL OR r.restaurant_name LIKE :searchText)
      """, nativeQuery = true)
  Page<RestaurantProjection> findNearbyWithFilter(
      @Param("lat") Double lat,
      @Param("lng") Double lng,
      @Param("radius") Double radius,
      @Param("categoryId") String categoryId,
      @Param("searchText") String searchText,
      Pageable pageable);

  @Query(value = """
      SELECT DISTINCT r.user_id AS restaurantId, r.restaurant_name AS restaurantName,
             r.address AS address, r.banner_id AS bannerId,
             0.0 AS distance_m,
             COALESCE(ratings.avg_rating, 0) AS rating,
             COALESCE(ratings.review_count, 0) AS review_count
      FROM restaurants r
      LEFT JOIN (
          SELECT o.restaurant_id, COALESCE(AVG(rev.rating), 0) as avg_rating, COUNT(rev.id) as review_count
          FROM orders o
          LEFT JOIN reviews rev ON o.id = rev.order_id
          GROUP BY o.restaurant_id
      ) ratings ON r.user_id = ratings.restaurant_id

      WHERE r.is_approved = true
      AND(COALESCE(:categoryId, '') = '' OR EXISTS (
          SELECT 1 FROM restaurant_has_categories rhc
          WHERE rhc.restaurant_id = r.user_id
          AND rhc.restaurant_category_id = :categoryId
        ))
        AND (COALESCE(:searchText, '') = '' OR r.restaurant_name LIKE :searchText)

      ORDER BY rating DESC
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      WHERE r.is_approved = true
      AND (COALESCE(:categoryId, '') = '' OR EXISTS (
          SELECT 1 FROM restaurant_has_categories rhc
          WHERE rhc.restaurant_id = r.user_id
          AND rhc.restaurant_category_id = :categoryId
        ))
        AND (COALESCE(:searchText, '') = '' OR r.restaurant_name LIKE :searchText)
      """, nativeQuery = true)
  Page<RestaurantProjection> findAllWithFilter(
      @Param("categoryId") String categoryId,
      @Param("searchText") String searchText,
      Pageable pageable);

  Page<Restaurant> findAllByApprovedFalse(Pageable pageable);

  Page<Restaurant> findAllByApprovedTrueAndRestaurantCategories_Id(String categoryId, Pageable pageable);

  Page<Restaurant> findAllByApprovedTrue(Pageable pageable);

  @Query(value = """
      SELECT r.* FROM restaurants r
      JOIN orders o ON r.user_id = o.restaurant_id
      JOIN reviews rev ON o.id = rev.order_id
      GROUP BY r.user_id
      HAVING CAST(SUM(CASE WHEN rev.sentiment = 'NEGATIVE' THEN 1 ELSE 0 END) AS DECIMAL) / COUNT(rev.id) > 0.8
      """, countQuery = """
      SELECT COUNT(*) FROM (
          SELECT r.user_id FROM restaurants r
          JOIN orders o ON r.user_id = o.restaurant_id
          JOIN reviews rev ON o.id = rev.order_id
          GROUP BY r.id
          HAVING CAST(SUM(CASE WHEN rev.sentiment = 'NEGATIVE' THEN 1 ELSE 0 END) AS DECIMAL) / COUNT(rev.id) > 0.8
      ) as temp
      """, nativeQuery = true)
  Page<Restaurant> findHighNegativeRestaurants(Pageable pageable);
}