package com.sharp.sharpshap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Column(name = "percentage_of_sale",precision = 3)
    private BigDecimal percentageOfSale = new BigDecimal("3");

    @Column(name = "margin_percentage",precision = 4 , scale = 2)
    private BigDecimal marginPercentage = new BigDecimal("1.00");

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    private List<CategorySubcategory> categorySubcategories;

}
