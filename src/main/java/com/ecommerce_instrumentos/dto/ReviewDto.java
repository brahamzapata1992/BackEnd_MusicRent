package com.ecommerce_instrumentos.dto;

import com.ecommerce_instrumentos.entity.Product;
import com.ecommerce_instrumentos.entity.User;
import lombok.Data;


@Data
public class ReviewDto {


    private Long id;
    private Long userId;
    private Long productId;
    private String userName;
    private String productName;
    private int rating;
    private String description;

}
