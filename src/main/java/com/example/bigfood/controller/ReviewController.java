package com.example.bigfood.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bigfood.dto.request.CreateReviewRequest;
import com.example.bigfood.dto.request.ReplyReviewRequest;
import com.example.bigfood.dto.response.ApiResponse;
import com.example.bigfood.dto.response.ReviewResponse;
import com.example.bigfood.service.ReviewService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;


    @PostMapping("/{orderId}")
    public ApiResponse<ReviewResponse> createNewReview(@PathVariable String orderId, @RequestBody CreateReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewService.userCreateReview(orderId, request))
                .message("Review created successfully")
                .build();
    }


    @GetMapping("/order/{orderId}")
    public ApiResponse<ReviewResponse> getOrderReview(@PathVariable String orderId) {
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewService.getReviewByOrderId(orderId))
                .message("Review fetched successfully")
                .build();
    }


    /**
     * hàm chỉnh sửa đánh giá của user
     *
     * @param orderID id của đơn hàng
     * @param request reqest chứa nội dung đánh giá mới
     * @return response của đánh giá vừa được chỉnh sửa
     */
    @PatchMapping("/{orderId}")
    public ApiResponse<ReviewResponse> userUpdateReview(@PathVariable String orderId, @RequestBody CreateReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewService.userUpdateReview(orderId, request))
                .message("Review updated successfully")
                .build();
    }



    /**
     * nhà hàng trả lời review của user
     *
     * @param orderId id của đơn hàng được đánh giá
     * @param request chứa nội dung phản hồi của nhà hàng
     * @return response mới của review
     */
    @PatchMapping("/{orderId}/reply")
    public ApiResponse<ReviewResponse> replyReview(@PathVariable String orderId, @RequestBody ReplyReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewService.replyReview(orderId, request))
                .message("Review updated successfully")
                .build();
    }
    


    @GetMapping("/restaurant/{restaurantId}/all")
    @PostAuthorize("hasRole('ADMIN')")
    public ApiResponse<Set<ReviewResponse>> getAllByRestaurantId(@PathVariable String restaurantId) {
        return ApiResponse.<Set<ReviewResponse>>builder()
                .results(reviewService.getAllByRestaurantId(restaurantId))
                .message("Get all reviews by restaurant id successfully")
                .build();
    }


    @GetMapping("/restaurant/all")
    public ApiResponse<Set<ReviewResponse>> getAllByRestaurant(@AuthenticationPrincipal Jwt jwt) {
        String restaurantId = jwt.getSubject();
        return ApiResponse.<Set<ReviewResponse>>builder()
                .results(reviewService.getAllByRestaurantId(restaurantId))
                .message("Get all reviews by restaurant id successfully")
                .build();
    }

    @GetMapping("/restaurant/filter")
    public ApiResponse<Set<ReviewResponse>> getAllByRestaurantAndSort(@AuthenticationPrincipal Jwt jwt ,  @RequestParam String sort) {
        String restaurantId = jwt.getSubject();
        return ApiResponse.<Set<ReviewResponse>>builder()
                .results(reviewService.getAllByRestaurantIdAndSort(restaurantId, sort))
                .message("Get all reviews by restaurant id successfully")
                .build();
    }

    @DeleteMapping("/{orderId}")
    public ApiResponse<Void> deleteReview(@PathVariable String orderId) {
        reviewService.deleteReviewByOrderId(orderId);
        return ApiResponse.<Void>builder()
                .message("Review deleted successfully")
                .build();
    }
}
