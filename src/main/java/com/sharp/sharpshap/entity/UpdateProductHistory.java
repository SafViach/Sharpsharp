package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class UpdateProductHistory {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Product product; //каждое изменение относится к одному продукту

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;// один user может много сделать изменений

    @Column(nullable = false)
    private LocalDateTime dateUpdate = LocalDateTime.now();

    private String oldBrand;
    private String newBrand;

    private String oldModel;
    private String newModel;

    private String oldCharacteristics;
    private String newCharacteristics;

    private int oldQuantity;
    private int newQuantity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EnumCurrency oldCurrency;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EnumCurrency newCurrency;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal oldPriceWithVat;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal newPriceWithVat;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal oldPriceSelling;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal newPriceSelling;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EnumStatusProduct oldStatusProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private EnumStatusProduct newStatusProduct;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CategorySubcategory oldCategorySubcategory;

    @ManyToOne
    @JoinColumn(nullable = false)
    private CategorySubcategory newCategorySubcategory;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TradePoint oldTradePoint;

    @ManyToOne
    @JoinColumn(nullable = false)
    private TradePoint newTradePoint;
}
