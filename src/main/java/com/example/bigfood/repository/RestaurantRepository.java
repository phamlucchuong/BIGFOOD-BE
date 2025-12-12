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
             COALESCE(AVG(rev.rating), 0) AS rating,
             count(rev.id) AS review_count
      FROM restaurants r
      LEFT JOIN orders o ON r.user_id = o.restaurant_id
      LEFT JOIN reviews rev ON o.id = rev.order_id
      WHERE ST_Distance_Sphere(
              r.location,
              ST_SRID(POINT(:lng, :lat), 4326)
            ) <= :radius
      GROUP BY r.user_id
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
      FROM restaurants r
      JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      LEFT JOIN orders o ON r.user_id = o.restaurant_id
      LEFT JOIN reviews rev ON o.id = rev.order_id

      WHERE rhc.restaurant_category_id = :categoryId
      GROUP BY r.user_id
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      WHERE rhc.restaurant_category_id = :categoryId
      """, nativeQuery = true)
  Page<RestaurantProjection> findByCategoryId(@Param("categoryId") String categoryId, Pageable pageable);

  @Query(value = """
      SELECT r.user_id AS restaurantId, r.restaurant_name AS restaurantName,
             r.address AS address, r.banner_id AS bannerId,
             ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) AS distance_m,
             COALESCE(AVG(rev.rating), 0) AS rating,
             COUNT(rev.id) AS review_count
      FROM restaurants r
      -- Join bảng trung gian category (Dùng LEFT JOIN để không mất dữ liệu nếu không lọc category)
      LEFT JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id

      -- Join rating
      LEFT JOIN orders o ON r.user_id = o.restaurant_id
      LEFT JOIN reviews rev ON o.id = rev.order_id

      WHERE ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) <= :radius

        -- LOGIC THÔNG MINH Ở ĐÂY:
        -- Nếu :categoryId là NULL, vế đầu đúng -> bỏ qua điều kiện sau (lấy tất cả)
        -- Nếu :categoryId có giá trị, vế đầu sai -> bắt buộc check điều kiện sau
        AND (:categoryId IS NULL OR rhc.restaurant_category_id = :categoryId)

        -- Tương tự với Search Text
        AND (:searchText IS NULL OR r.restaurant_name LIKE :searchText)

      GROUP BY r.user_id
      ORDER BY distance_m ASC
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      LEFT JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      WHERE ST_Distance_Sphere(r.location, ST_SRID(POINT(:lng, :lat), 4326)) <= :radius
        AND (:categoryId IS NULL OR rhc.restaurant_category_id = :categoryId)
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
      SELECT r.user_id AS restaurantId, r.restaurant_name AS restaurantName,
             r.address AS address, r.banner_id AS bannerId,
             0.0 AS distance_m,
             COALESCE(AVG(rev.rating), 0) AS rating,
             COUNT(rev.id) AS review_count
      FROM restaurants r
      LEFT JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      LEFT JOIN orders o ON r.user_id = o.restaurant_id
      LEFT JOIN reviews rev ON o.id = rev.order_id

      WHERE (COALESCE(:categoryId, '') = '' OR rhc.restaurant_category_id = :categoryId)
        AND (COALESCE(:searchText, '') = '' OR r.restaurant_name LIKE :searchText)

      GROUP BY r.user_id
      ORDER BY rating DESC
      """, countQuery = """
      SELECT COUNT(DISTINCT r.user_id)
      FROM restaurants r
      LEFT JOIN restaurant_has_categories rhc ON r.user_id = rhc.restaurant_id
      WHERE (COALESCE(:categoryId, '') = '' OR rhc.restaurant_category_id = :categoryId)
        AND (COALESCE(:searchText, '') = '' OR r.restaurant_name LIKE :searchText)
      """, nativeQuery = true)
  Page<RestaurantProjection> findAllWithFilter(
      @Param("categoryId") String categoryId,
      @Param("searchText") String searchText,
      Pageable pageable);
}
