package com.ecommerce_instrumentos.controller.admin;

import com.ecommerce_instrumentos.dto.CategoryDto;
import com.ecommerce_instrumentos.entity.Category;
import com.ecommerce_instrumentos.services.admin.category.CategoryService;
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
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(
            @RequestPart("name") String name,
            @RequestPart("description") String description,
            @RequestPart("img") MultipartFile img) {

        try {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setName(name);
            categoryDto.setDescription(description);
            categoryDto.setImg(img);

            Category createdCategory = categoryService.createcategory(categoryDto);
            return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
        } catch (IOException e) {
            // Manejar la excepción de IO si es necesario
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("La categoría ha sido eliminada correctamente.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se puede eliminar la categoría porque tiene productos asociados.");
        }
    }
}
