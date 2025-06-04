package com.sharp.sharpshap.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "sales_products")
public class SaleProduct {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sale sale;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
