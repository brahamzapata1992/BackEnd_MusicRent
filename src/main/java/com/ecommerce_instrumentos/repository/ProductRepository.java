package com.ecommerce_instrumentos.repository;

import com.ecommerce_instrumentos.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByNameContaining(String title);

    List<Product> findByCategory_IdIn(List<Long> categoryIds);
    @EntityGraph(attributePaths = "features")
    List<Product> findByFeatures_FeatureIdIn(List<Long> featureIds);

    List<Product> findByCategory_Id(Long categoryId);

}
