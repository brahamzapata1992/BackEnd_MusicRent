package com.ecommerce_instrumentos.dto;

import lombok.Data;

@Data
public class ProductImageDto {

    private byte[] imageData;

    // Constructor
    public ProductImageDto(byte[] imageData) {
        this.imageData = imageData;
    }
}
