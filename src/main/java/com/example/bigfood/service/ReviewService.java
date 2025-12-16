package com.example.bigfood.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.bigfood.dto.request.CreateReviewRequest;
import com.example.bigfood.dto.request.ReplyReviewRequest;
import com.example.bigfood.dto.response.ReviewResponse;
import com.example.bigfood.entity.Order;
import com.example.bigfood.entity.Restaurant;
import com.example.bigfood.entity.Review;
import com.example.bigfood.enums.ErrorCode;
import com.example.bigfood.enums.OrderStatus;
import com.example.bigfood.exception.AppException;
import com.example.bigfood.mapper.ReviewMapper;
import com.example.bigfood.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    OrderService orderService;
    RestaurantService restaurantService;

    public ReviewResponse userCreateReview(String orderId, CreateReviewRequest request) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        if (!order.getStatus().equals(OrderStatus.COMPLETED))
            throw new AppException(ErrorCode.ORDER_NOT_COMPLETED);

        if (order.getReview() != null)
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);

        Review review = reviewMapper.toReview(request);
        if (review == null)
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);

        review.setOrder(order);
        order.setReview(review);
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    public ReviewResponse userUpdateReview(String orderId, CreateReviewRequest request) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        if (!order.getStatus().equals(OrderStatus.COMPLETED))
            throw new AppException(ErrorCode.ORDER_NOT_COMPLETED);

        Review review = order.getReview();
        if (review == null)
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);

        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    public ReviewResponse getReviewByOrderId(String orderId) {
        Order order = orderService.getOrderById(orderId);
        Review review = reviewRepository.findByOrder(order)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return reviewMapper.toReviewResponse(review);
    }

    public ReviewResponse replyReview(String orderId, ReplyReviewRequest request) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        Review review = reviewRepository.findByOrder(order)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (review.getReplyAt() != null)
            throw new AppException(ErrorCode.REVIEW_ALREADY_REPLIED);

        review.setReplyText(request.getReplyText());
        review.setReplyAt(LocalDateTime.now());
        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    public Set<ReviewResponse> getAllByRestaurantId(String restaurantId) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(restaurantId);

        Set<Review> reviews = reviewRepository.findAllByOrder_Restaurant(restaurant);
        return reviewMapper.toReviewResponseSet(reviews);
    }
    public Set<ReviewResponse> getAllByRestaurantIdAndSort(String restaurantId, String filter) {
        Restaurant restaurant = restaurantService.getRestaurantByUserId(restaurantId);
        String normalizedFilter = (filter == null || filter.isEmpty()) ? "all" : filter.toLowerCase();
        
        Set<Review> reviews = reviewRepository.findAllByOrder_RestaurantAndSortReviews(restaurant, normalizedFilter);
        return reviewMapper.toReviewResponseSet(reviews);
    }

    public void deleteReviewByOrderId(String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null)
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);

        Review review = order.getReview();
        if (review == null)
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);

        order.setReview(null);
        reviewRepository.delete(review);
    }

}
