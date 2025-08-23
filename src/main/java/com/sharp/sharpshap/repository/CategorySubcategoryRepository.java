package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategorySubcategoryRepository extends JpaRepository<CategorySubcategory, UUID> {

    @Query("SELECT cs.subcategory FROM CategorySubcategory cs WHERE cs.category.id = :categoryId")
    List<Subcategory> findSubcategoriesByCategoryId(@Param("categoryId") UUID categoryId);

    Optional<CategorySubcategory> findByCategoryAndSubcategory(Category category, Subcategory subcategory);
    Optional<CategorySubcategory> findByCategoryAndSubcategoryIsNull(Category category);

    Optional<CategorySubcategory> findByCategory(Category category);
}
