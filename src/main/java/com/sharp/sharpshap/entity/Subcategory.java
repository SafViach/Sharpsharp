package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , length = 50)
    @NotNull
    private String name;

    @Column(precision = 15, scale = 2)
    private BigDecimal coefficientSales = BigDecimal.ZERO;

    @OneToMany(mappedBy = "subcategory")
    private List<CategorySubcategory> categorySubcategories;
}
