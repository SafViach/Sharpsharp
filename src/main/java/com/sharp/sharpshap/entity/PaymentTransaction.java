package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class PaymentTransaction {
    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal cashAmount;
    private BigDecimal cashlessAmount;
    private BigDecimal creditAmount;

    @OneToMany(mappedBy = "paymentTransaction", fetch = FetchType.LAZY)
    private List<PaymentTransactionMoneyLocation> transactionMoneyLocations;
}
