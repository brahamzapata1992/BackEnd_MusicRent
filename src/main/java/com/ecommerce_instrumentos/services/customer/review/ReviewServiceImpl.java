package com.ecommerce_instrumentos.services.customer.review;

import com.ecommerce_instrumentos.dto.ReviewDto;
import com.ecommerce_instrumentos.entity.Product;
import com.ecommerce_instrumentos.entity.Review;
import com.ecommerce_instrumentos.entity.User;
import com.ecommerce_instrumentos.repository.ProductRepository;
import com.ecommerce_instrumentos.repository.ReviewRepository;
import com.ecommerce_instrumentos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public ReviewDto createReview(Long userId, Long productId, ReviewDto reviewDto) {
        Review review = new Review();
        review.setRating(reviewDto.getRating());
        review.setDescription(reviewDto.getDescription());

        // Recuperar el usuario y el producto desde la base de datos
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        review.setUser(user);
        review.setProduct(product);

        // Guardar la nueva revisión
        Review savedReview = reviewRepository.save(review);

        // Convertir la revisión guardada a un DTO y devolverlo
        return convertToReviewDto(savedReview);
    }

    // Otros métodos del servicio

    private ReviewDto convertToReviewDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setUserId(review.getUser().getId());
        reviewDto.setProductId(review.getProduct().getId());
        reviewDto.setUserName(review.getUser().getName());
        reviewDto.setProductName(review.getProduct().getName());
        reviewDto.setRating(review.getRating());
        reviewDto.setDescription(review.getDescription());

        // Otros campos según sea necesario

        return reviewDto;
    }


    @Override
    public List<ReviewDto> getReviewsByProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProduct_Id(productId);
        return reviews.stream()
                .map(this::convertToReviewDto)
                .collect(Collectors.toList());
    }


}
