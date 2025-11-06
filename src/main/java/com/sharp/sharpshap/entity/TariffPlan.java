package com.sharp.sharpshap.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TariffPlan {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}
