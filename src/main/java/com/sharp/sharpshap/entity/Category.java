package com.sharp.sharpshap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , length = 50 , unique = true)
    private String name;

    @Column(name = "coefficient_sale",precision = 15, scale = 2)
    private BigDecimal coefficientSale = BigDecimal.ZERO;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<CategorySubcategory> categorySubcategories;

}
