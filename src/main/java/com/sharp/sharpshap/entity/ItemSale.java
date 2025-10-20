package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumTypeSale;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "item_sale")
public class ItemSale {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sale sale;

    @JoinColumn(name = "what_was_sold_id", nullable = false)
    private UUID whatWasSoldId;

    private int quantity;

    @ManyToOne
    private EnumTypeSale typeSale;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne
    private PaymentTransaction paymentTransaction;
}
