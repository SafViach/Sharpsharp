package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Sale{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private LocalDateTime saleDateTime;

    @ManyToOne
    @JoinColumn(name = "trade_point_id",nullable = false)
    private TradePoint tradePoint;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "sale" , cascade = CascadeType.ALL)//---------------------------------
    private List<SaleProduct> saleProducts;

    @OneToOne
    @JoinColumn(nullable = false)
    private PaymentTransaction transaction;

}
