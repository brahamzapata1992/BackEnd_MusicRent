package com.ecommerce_instrumentos.services.admin.feature;

import com.ecommerce_instrumentos.dto.FeatureDto;
import com.ecommerce_instrumentos.entity.Feature;
import com.ecommerce_instrumentos.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService{
    private final FeatureRepository featureRepository;


    @Override
    public Feature createFeature(FeatureDto featureDto) {
        Feature feature=new Feature();
        feature.setName(featureDto.getName());
        feature.setIcon(featureDto.getIcon());

        return featureRepository.save(feature);
    }

    @Override
    public List<FeatureDto> getAllFeatures() {
        List<Feature> features = featureRepository.findAll();
        return features.stream()
                .map(this::convertToFeatureDto)
                .collect(Collectors.toList());
    }



    // Método para convertir una entidad Feature a un DTO FeatureDto
    private FeatureDto convertToFeatureDto(Feature feature) {
        FeatureDto featureDto = new FeatureDto();
        featureDto.setId(feature.getId());
        featureDto.setName(feature.getName());
        featureDto.setIcon(feature.getIcon());
        return featureDto;
    }
    @Override
    public FeatureDto updateFeature(Long id, FeatureDto featureDto) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);
        if (optionalFeature.isPresent()) {
            Feature existingFeature = optionalFeature.get();
            if (featureDto.getName() != null) {
                existingFeature.setName(featureDto.getName());
            }
            if (featureDto.getIcon() != null) {
                // Elimina las imágenes antiguas
                existingFeature.setIcon(featureDto.getIcon());
            }
            Feature updatedFeature = featureRepository.save(existingFeature);
            return updatedFeature.getDto();
        }

        throw new NoSuchElementException("Caracteristica no encontrada con id: " + id);
    }

    @Override
    public Boolean deleteFeature(Long id) {
        Optional<Feature> optionalFeature = featureRepository.findById(id);
        if (optionalFeature.isPresent()) {
            Feature feature = optionalFeature.get();

            // Elimina las imágenes asociadas al producto
            feature.setIcon(new byte[0]);

            // Elimina el producto
            featureRepository.deleteById(id);
        }
        return true;
    }

    @Override
    public List<FeatureDto> findFeatures(List<Long> featuresIds) {
        List<Feature> features = featureRepository.findAllById(featuresIds);
        return features.stream().map(Feature::getDto).collect(Collectors.toList());

    }
}
