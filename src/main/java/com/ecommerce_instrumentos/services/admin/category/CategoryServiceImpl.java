package com.ecommerce_instrumentos.services.admin.category;

import com.ecommerce_instrumentos.dto.CategoryDto;
import com.ecommerce_instrumentos.entity.Category;
import com.ecommerce_instrumentos.entity.Product;
import com.ecommerce_instrumentos.repository.CategoryRepository;
import com.ecommerce_instrumentos.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public Category createcategory(CategoryDto categoryDto) throws IOException {
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImg(categoryDto.getImg().getBytes());


        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }



    @Override
    public void deleteCategory(Long categoryId) {
        // Verificar si hay productos asociados a la categoría
        List<Product> products = productRepository.findByCategory_Id(categoryId);
        if (!products.isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene productos asociados.");
        }

        // Si no hay productos asociados, eliminar la categoría
        categoryRepository.deleteById(categoryId);
    }





}
