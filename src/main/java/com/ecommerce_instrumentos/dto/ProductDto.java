package com.ecommerce_instrumentos.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private Long price;
    private String description;
    private List<ProductImageDto> images;
    private Long categoryId;
    private String categoryName;
    private List<ProductFeatureDto> features;
    private int rating;

    // Constructor, getters y setters

    // Método para convertir MultipartFile a bytes
    public void convertImagesToBytes() throws IOException {
        if (images != null && !images.isEmpty()) {
            images = images.stream()
                    .map(image -> {
                        return new ProductImageDto(image.getImageData());
                    })
                    .collect(Collectors.toList());
        }
    }


}



