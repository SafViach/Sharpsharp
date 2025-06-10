package com.sharp.sharpshap.enums;

import com.sharp.sharpshap.entity.PaymentTransaction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class EnumMoneyLocation{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , unique = true)
    private String path;

    @ManyToOne
    @JoinColumn(name = "payment_transaction_id")
    private PaymentTransaction paymentTransaction;
}
