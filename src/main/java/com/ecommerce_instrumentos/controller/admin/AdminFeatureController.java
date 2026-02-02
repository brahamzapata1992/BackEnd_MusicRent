package com.ecommerce_instrumentos.controller.admin;

import com.ecommerce_instrumentos.dto.FeatureDto;
import com.ecommerce_instrumentos.entity.Feature;
import com.ecommerce_instrumentos.services.admin.feature.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminFeatureController {
    private final FeatureService featureService;
    @PostMapping("/feature")
    public ResponseEntity<FeatureDto> createFeature(
            @RequestParam("name") String name,
            @RequestParam("icon") MultipartFile icon) {

        try {
            // Convertir el icono a bytes
            byte[] iconBytes = icon.getBytes();

            // Crear un objeto FeatureDto con los datos recibidos
            FeatureDto featureDto = new FeatureDto();
            featureDto.setName(name);
            featureDto.setIcon(iconBytes);

            // Llama al servicio para procesar y guardar la nueva característica
            Feature createdFeature = featureService.createFeature(featureDto);

            // Convertir el objeto Feature a un FeatureDto
            FeatureDto createdFeatureDto = convertToFeatureDto(createdFeature);

            // Devuelve la respuesta con el objeto de la nueva característica creada
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFeatureDto);
        } catch (IOException e) {
            // Manejo de errores en caso de problemas al leer el archivo o convertirlo a bytes
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Método para convertir un objeto Feature a un FeatureDto
    private FeatureDto convertToFeatureDto(Feature feature) {
        FeatureDto featureDto = new FeatureDto();
        featureDto.setId(feature.getId());
        featureDto.setName(feature.getName());
        // Asignar el icono (byte[]) de acuerdo a tu lógica específica
        return featureDto;
    }

    @GetMapping("/features")
    public ResponseEntity<List<FeatureDto>> getAllFeatures() {
        List<FeatureDto> featureDtos = featureService.getAllFeatures();
        return ResponseEntity.ok(featureDtos);
    }
    @PostMapping("/features/{featureId}")
    public ResponseEntity<FeatureDto> updateFeature(@PathVariable Long featureId,  @RequestParam("name") String name, @RequestParam("icon") MultipartFile icon) throws IOException{
        FeatureDto featureDto= new FeatureDto();
        featureDto.setId(featureId);
        featureDto.setName(name);
        featureDto.setIcon(icon.getBytes());
        FeatureDto newFeatureDto=featureService.updateFeature(featureDto.getId(),featureDto);
        return ResponseEntity.ok(featureDto);
    }
    @DeleteMapping("/features/{featureId}")
    public ResponseEntity<FeatureDto> deleteFeauture(@PathVariable Long featureId){
        boolean deleted = featureService.deleteFeature(featureId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/feature")
    public ResponseEntity <List<FeatureDto>> findFeatures(@RequestParam List<Long> featuresIds){
       List<FeatureDto> features = featureService.findFeatures(featuresIds);
        return ResponseEntity.ok(features);
    }

}
