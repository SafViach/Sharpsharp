package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, UUID> {

    Optional<Subcategory> findByName(String name);

    Optional<Subcategory> findByNameIgnoreCase(String name);
}
