package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifeEquipment {
    @Id
    @GeneratedValue
    private UUID id;

    private String brand;
    private String model;
    private String characteristics;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSelling;

    @Column(nullable = false)
    private String sku;

    @ManyToOne
    private User user;


    @ManyToOne
    @JoinColumn(name = "trade_point_id", nullable = false)
    private TradePoint tradePoint;
}
