package com.ecommerce_instrumentos.services.admin.category;

import com.ecommerce_instrumentos.dto.CategoryDto;
import com.ecommerce_instrumentos.entity.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    Category createcategory(CategoryDto categoryDto) throws IOException;

    List<Category> getAllCategories();

    void deleteCategory(Long categoryId);

}
