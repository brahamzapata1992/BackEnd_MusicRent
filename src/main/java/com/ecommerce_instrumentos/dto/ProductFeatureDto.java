package com.ecommerce_instrumentos.dto;

import lombok.Data;

@Data
public class ProductFeatureDto {

    private Long id;
    private Long productId;
    private Long featureId;

    public ProductFeatureDto(Long id, Long productId, Long featureId) {
        this.id=id;
        this.productId = productId;
        this.featureId = featureId;
    }

    // Getters y setters...
}
