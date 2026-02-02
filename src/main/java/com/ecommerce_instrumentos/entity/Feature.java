package com.ecommerce_instrumentos.entity;

import com.ecommerce_instrumentos.dto.FeatureDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="features")
public class Feature {
    @Id
    @Column(name = "id_feature")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "blob") // Cambiamos el tipo de dato a blob
    private byte[] icon;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductFeature> productFeatures;

    public FeatureDto getDto() {
        FeatureDto featureDto = new FeatureDto();
        featureDto.setId(id);
        featureDto.setName(name);
        featureDto.setIcon(icon);

        return featureDto;
    }
}
