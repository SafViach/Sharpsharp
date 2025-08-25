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

    @Column(name = "percentage_of_sale",precision = 3 , nullable = false)
    private BigDecimal percentageOfSale = new BigDecimal("3");

    @Column(name = "margin_percentage",precision = 4 , scale = 2 , nullable = false)
    private BigDecimal marginPercentage = new BigDecimal("1.00");

    @OneToMany(mappedBy = "subcategory")
    private List<CategorySubcategory> categorySubcategories;
}
