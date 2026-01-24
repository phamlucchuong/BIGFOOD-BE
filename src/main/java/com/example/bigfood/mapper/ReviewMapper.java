package com.example.bigfood.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bigfood.dto.request.CreateReviewRequest;
import com.example.bigfood.dto.response.ReviewResponse;
import com.example.bigfood.entity.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdateAt", ignore = true)
    @Mapping(target = "replyText", ignore = true)
    @Mapping(target = "replyAt", ignore = true)
    @Mapping(target = "order", ignore = true)
    Review toReview(CreateReviewRequest request);

    ReviewResponse toReviewResponse(Review review);

    Set<ReviewResponse> toReviewResponseSet(Set<Review> reviews);
}
