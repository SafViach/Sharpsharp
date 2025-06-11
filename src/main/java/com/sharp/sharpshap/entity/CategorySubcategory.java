package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class CategorySubcategory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;
}

