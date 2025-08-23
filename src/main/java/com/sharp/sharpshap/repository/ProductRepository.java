package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByCategorySubcategory_Category_Id(UUID uuidCategory);
    boolean existsByCategorySubcategory_Subcategory_Id(UUID uuidSubcategory);
}
