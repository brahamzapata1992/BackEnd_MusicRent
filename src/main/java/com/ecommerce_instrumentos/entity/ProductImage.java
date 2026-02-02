package com.ecommerce_instrumentos.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] imageData;

    // Relación con la entidad Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


}


