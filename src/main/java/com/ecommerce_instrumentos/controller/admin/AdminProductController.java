package com.ecommerce_instrumentos.controller.admin;

import com.ecommerce_instrumentos.dto.FeatureDto;
import com.ecommerce_instrumentos.dto.ProductDto;
import com.ecommerce_instrumentos.dto.ProductFeatureDto;
import com.ecommerce_instrumentos.dto.ProductImageDto;
import com.ecommerce_instrumentos.exeption.ErrorResponse;
import com.ecommerce_instrumentos.exeption.ProductNotFoundException;
import com.ecommerce_instrumentos.services.admin.adminproduct.AdminProductService;
import com.ecommerce_instrumentos.services.admin.feature.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final AdminProductService adminProductService;
    private final FeatureService featureService;

    public static final int MAX_IMAGES = 5;

    private static ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
        ErrorResponse body = new ErrorResponse();
        body.setStatus(status.value());
        body.setMessage(message);
        return ResponseEntity.status(status).body(body);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (images == null || images.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "Agrega al menos una imagen del producto.");
        }
        if (images.size() > MAX_IMAGES) {
            return error(HttpStatus.BAD_REQUEST, "Puedes subir máximo " + MAX_IMAGES + " imágenes por producto.");
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategoryId(categoryId);

        // Convertir imágenes a bytes en el controlador
        List<ProductImageDto> imageDtos = images.stream()
                .map(image -> {
                    try {
                        return new ProductImageDto(image.getBytes());
                    } catch (IOException e) {
                        // Manejo de errores, por ejemplo, log o lanzar una excepción
                        throw new RuntimeException("Error al convertir imagen a bytes", e);
                    }
                })
                .collect(Collectors.toList());

        productDto.setImages(imageDtos);

        // Convertir MultipartFile a bytes en el DTO antes de llamar al servicio
        productDto.convertImagesToBytes();

        try {
            // Llama al servicio para procesar y guardar el producto con sus imágenes
            ProductDto createdProduct = adminProductService.addProduct(productDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (NoSuchElementException e) {
            return error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar el producto.");
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = adminProductService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDto>> getAllProductsByName(@PathVariable String name) {
        List<ProductDto> products = adminProductService.getAllProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean deleted = adminProductService.deleteProduct(productId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestParam("name") String name,
            @RequestParam("price") Long price,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (images != null && images.size() > MAX_IMAGES) {
            return error(HttpStatus.BAD_REQUEST, "Puedes subir máximo " + MAX_IMAGES + " imágenes por producto.");
        }

        ProductDto productDto = new ProductDto();
        productDto.setName(name);
        productDto.setPrice(price);
        productDto.setDescription(description);
        productDto.setCategoryId(categoryId);

        // Convertir imágenes a bytes en el controlador si se proporcionan
        if (images != null && !images.isEmpty()) {
            List<ProductImageDto> imageDtos = images.stream()
                    .map(image -> {
                        try {
                            return new ProductImageDto(image.getBytes());
                        } catch (IOException e) {
                            // Manejo de errores, por ejemplo, log o lanzar una excepción
                            throw new RuntimeException("Error al convertir imagen a bytes", e);
                        }
                    })
                    .collect(Collectors.toList());

            productDto.setImages(imageDtos);

            // Convertir MultipartFile a bytes en el DTO antes de llamar al servicio
            productDto.convertImagesToBytes();
        }

        try {
            // Llama al servicio para procesar y actualizar el producto con sus imágenes
            ProductDto updatedProduct = adminProductService.updateProduct(productId, productDto);

            return ResponseEntity.ok(updatedProduct);
        } catch (NoSuchElementException e) {
            return error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo actualizar el producto.");
        }
    }


    @GetMapping("/byCategories")
    public ResponseEntity<List<ProductDto>> getProductsByCategories(@RequestParam List<Long> categoryIds) {
        List<ProductDto> products = adminProductService.getProductsByCategories(categoryIds);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            ProductDto productDto = adminProductService.getProductById(productId);
            return ResponseEntity.ok(productDto);
        } catch (ProductNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
            errorResponse.setMessage("Producto no encontrado con ID: " + productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
    @GetMapping("/byFeatures")
    public ResponseEntity<List<ProductDto>> getProductsByFeatures(@RequestParam List<Long> featuresIds) {
        List<ProductDto> products = adminProductService.getProductsByFeatures(featuresIds);
        System.out.println("Holas para");
        return ResponseEntity.ok(products);


    }
    @GetMapping("/{productId}/features")
    public ResponseEntity<List<FeatureDto>> getFeatureOfAProduct(@PathVariable Long productId){
        List<ProductFeatureDto> featureDtoList=adminProductService.getFeaturesOfAProduct(productId);
        List<Long> featuresIds = featureDtoList.stream()
                .map(ProductFeatureDto::getFeatureId)
                .collect(Collectors.toList());
        List<FeatureDto> features = featureService.findFeatures(featuresIds);
        return ResponseEntity.ok(features);
    }
}

