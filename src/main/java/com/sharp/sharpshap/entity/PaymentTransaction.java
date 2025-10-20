package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumTypePayment;
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

    @ManyToOne
    private EnumTypePayment typePayment;

    private BigDecimal cashAmount;
    private BigDecimal cashlessAmount;
    private BigDecimal creditAmount;

    @OneToMany(mappedBy = "paymentTransaction")
    private List<PaymentTransactionMoneyLocation> moneyLocations;
}
