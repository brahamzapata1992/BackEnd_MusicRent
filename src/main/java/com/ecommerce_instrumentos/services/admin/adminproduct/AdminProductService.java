package com.ecommerce_instrumentos.services.admin.adminproduct;

import com.ecommerce_instrumentos.dto.ProductDto;
import com.ecommerce_instrumentos.dto.ProductFeatureDto;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {

    ProductDto addProduct(ProductDto productDto) throws IOException;

    List<ProductDto> getAllProducts();

    List<ProductDto> getAllProductsByName(String name);

    boolean deleteProduct(Long id);

    ProductDto updateProduct(Long id, ProductDto productDto) throws IOException;

    List<ProductDto> getProductsByCategories(List<Long> categoryIds);

    ProductDto getProductById(Long id);
    List<ProductDto> getProductsByFeatures(List<Long> featuresIds);
    List<ProductFeatureDto> getFeaturesOfAProduct(Long id);


}
