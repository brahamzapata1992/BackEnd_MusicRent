package com.ecommerce_instrumentos.services.customer.review;

import com.ecommerce_instrumentos.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(Long userId, Long productId, ReviewDto reviewDto);
    List<ReviewDto> getReviewsByProduct(Long productId);

}
