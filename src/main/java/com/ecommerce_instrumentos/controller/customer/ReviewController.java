package com.ecommerce_instrumentos.controller.customer;

import com.ecommerce_instrumentos.dto.ReviewDto;
import com.ecommerce_instrumentos.services.customer.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("create")
    public ResponseEntity<ReviewDto> createReview(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestBody ReviewDto reviewDto) {
        try {
            ReviewDto createdReview = reviewService.createReview(userId, productId, reviewDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (Exception e) {
            // Manejo de errores, por ejemplo, log o retorno de un código de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/listar/{productId}")
    public List<ReviewDto> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }



}
