package com.sharp.sharpshap.enums;

import com.sharp.sharpshap.entity.PaymentTransaction;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class EnumTypePayment{

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , unique = true)
    private String type;

    @ManyToOne
    @JoinColumn(name = "type_payment_id")
    private PaymentTransaction paymentTransaction;

}
