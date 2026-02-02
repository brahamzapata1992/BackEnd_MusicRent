package com.ecommerce_instrumentos.services.admin.feature;

import com.ecommerce_instrumentos.dto.FeatureDto;
import com.ecommerce_instrumentos.entity.Feature;

import java.util.List;

public interface FeatureService {

    Feature createFeature(FeatureDto featureDto);

    List<FeatureDto> getAllFeatures();

    FeatureDto updateFeature(Long id, FeatureDto featureDto);
    Boolean deleteFeature(Long id);
    List<FeatureDto> findFeatures(List<Long> featuresIds);

}
