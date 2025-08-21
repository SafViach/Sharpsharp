package com.sharp.sharpshap.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class CategorySubcategory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JsonBackReference
    private Subcategory subcategory;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Category category;
}

