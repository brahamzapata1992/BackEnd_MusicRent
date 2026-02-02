package com.ecommerce_instrumentos.entity;

import com.ecommerce_instrumentos.dto.ProductDto;
import com.ecommerce_instrumentos.dto.ProductFeatureDto;
import com.ecommerce_instrumentos.dto.ProductImageDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long price;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Relación uno a muchos con ProductImage
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id") // Esta columna en la tabla product_image se usa como clave externa
    private List<ProductImage> images;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> features;






    public ProductDto getDto() {
        ProductDto productDto = new ProductDto();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategoryId(category.getId());
        productDto.setCategoryName(category.getName());

        List<ProductImageDto> imageDtos = images.stream()
                .map(image -> new ProductImageDto(image.getImageData()))
                .collect(Collectors.toList());

        productDto.setImages(imageDtos);
        List<ProductFeatureDto> featureDtos = features.stream()
                .map(feature ->  new ProductFeatureDto(feature.getId(),feature.getProduct().getId(), feature.getFeature().getId()))
                .collect(Collectors.toList());

        productDto.setFeatures(featureDtos);


        return productDto;
    }
}



