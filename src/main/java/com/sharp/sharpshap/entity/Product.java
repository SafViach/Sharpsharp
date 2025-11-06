package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

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
    @JoinColumn(name = "status_id", nullable = false)
    private EnumStatusProduct statusProduct;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dateOfArrival;

    @ManyToOne
    @JoinColumn(name = "user_accepted_product_id", nullable = false)
    private User userAcceptedProduct;

    @ManyToOne
    @JoinColumn(name = "user_sale_product_id")
    private User userSaleProduct;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "category_subcategory_id" , nullable = false)
    private CategorySubcategory categorySubcategory;

    @ManyToOne
    @JoinColumn(name = "trade_point_id", nullable = false)
    private TradePoint tradePoint;

    @Column(nullable = false)
    private String sku;

    @Version
    private Integer version = 0;
}
