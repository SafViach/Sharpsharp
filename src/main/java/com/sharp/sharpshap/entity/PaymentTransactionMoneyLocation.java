package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumMoneyLocation;
import com.sharp.sharpshap.enums.EnumTypePayment;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class PaymentTransactionMoneyLocation {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "payment_transaction_id")
    private PaymentTransaction paymentTransaction;

    @ManyToOne
    @JoinColumn(name = "enum_money_location_id")
    private EnumMoneyLocation moneyLocation;

    @ManyToOne
    @JoinColumn(name = "enum_type_payment_id")
    private EnumTypePayment typePayment;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;
}
