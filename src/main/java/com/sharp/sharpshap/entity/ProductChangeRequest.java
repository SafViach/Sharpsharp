package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
public class ProductChangeRequest {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Product product;

    private String brand;
    private String model;
    private String characteristics;

    @Column(nullable = false)
    private int quantity;

    @OneToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private EnumCurrency currency;

    private BigDecimal currencyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceWithVat;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceSelling;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private EnumStatusProduct statusProduct;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "category_subcategory_id", nullable = false)
    private CategorySubcategory categorySubcategory;

    private String sku;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    private TradePoint tradePoint;
}
