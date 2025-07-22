package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.CategorySubcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategorySubcategoryRepository extends JpaRepository<CategorySubcategory, UUID> {
    Optional<CategorySubcategory> findByCategoryIdAndSubcategoryId(UUID categoryUUID, UUID subcategoryUUID);
}
