package com.ecommerce_instrumentos.services.admin.adminproduct;

import com.ecommerce_instrumentos.dto.ProductDto;
import com.ecommerce_instrumentos.dto.ProductFeatureDto;
import com.ecommerce_instrumentos.dto.ProductImageDto;
import com.ecommerce_instrumentos.entity.Category;
import com.ecommerce_instrumentos.entity.Product;
import com.ecommerce_instrumentos.entity.ProductImage;
import com.ecommerce_instrumentos.exeption.ProductNotFoundException;
import com.ecommerce_instrumentos.repository.CategoryRepository;
import com.ecommerce_instrumentos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductDto addProduct(ProductDto productDto) throws IOException {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + productDto.getCategoryId()));

        product.setCategory(category);

        // Genera nombres de imágenes automáticamente y guarda las imágenes
        List<ProductImage> productImages = new ArrayList<>();
        for (ProductImageDto imageDto : productDto.getImages()) {
            byte[] imageData = imageDto.getImageData();

            ProductImage productImage = new ProductImage();
            productImage.setImageData(imageData);
            productImage.setProduct(product);

            productImages.add(productImage);
        }

        product.setImages(productImages);

        // Guarda el producto y las imágenes
        Product savedProduct = productRepository.save(product);

        return savedProduct.getDto();
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductsByName(String name) {
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public boolean deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            // Elimina las imágenes asociadas al producto
            product.getImages().forEach(image -> image.setProduct(null));
            product.getImages().clear();

            // Elimina el producto
            productRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            // Actualiza los campos solo si no son nulos en el DTO
            if (productDto.getName() != null) {
                existingProduct.setName(productDto.getName());
            }
            if (productDto.getDescription() != null) {
                existingProduct.setDescription(productDto.getDescription());
            }
            if (productDto.getPrice() != null) {
                existingProduct.setPrice(productDto.getPrice());
            }

            // Actualiza la imagen solo si se proporciona una nueva imagen
            if (productDto.getImages() != null && !productDto.getImages().isEmpty()) {
                // Elimina las imágenes antiguas
                existingProduct.getImages().clear();

                // Agrega las nuevas imágenes
                for (ProductImageDto imageDto : productDto.getImages()) {
                    byte[] imageData = imageDto.getImageData();

                    ProductImage productImage = new ProductImage();
                    productImage.setImageData(imageData);
                    productImage.setProduct(existingProduct);

                    existingProduct.getImages().add(productImage);
                }
            } else {
                // Conserva las imágenes existentes si no se proporcionan nuevas
                productDto.setImages(existingProduct.getImages().stream()
                        .map(image -> new ProductImageDto(image.getImageData()))
                        .collect(Collectors.toList()));
            }

            // Actualiza la categoría solo si se proporciona una nueva categoría
            if (productDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(productDto.getCategoryId())
                        .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + productDto.getCategoryId()));
                existingProduct.setCategory(category);
            }

            // Guarda y devuelve la entidad actualizada
            Product updatedProduct = productRepository.save(existingProduct);
            return updatedProduct.getDto();
        }

        throw new NoSuchElementException("Producto no encontrado con id: " + id);
    }
    @Override
    public List<ProductDto> getProductsByCategories(List<Long> categoryIds) {
        List<Product> products = productRepository.findByCategory_IdIn(categoryIds);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product.getDto();
        } else {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + id);
        }
    }
    @Override
    public List<ProductDto> getProductsByFeatures(List<Long> featureIds) {
        List<Product> products = productRepository.findByFeatures_FeatureIdIn(featureIds);
        System.out.println("Hola si");


        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }
    @Override
    public List<ProductFeatureDto> getFeaturesOfAProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return product.getDto().getFeatures();
        } else {
            throw new ProductNotFoundException("Producto no encontrado con ID: " + id);
        }
    }



}

