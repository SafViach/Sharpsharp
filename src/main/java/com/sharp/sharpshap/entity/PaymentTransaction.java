package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumMoneyLocation;
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

    @OneToMany(mappedBy = "paymentTransaction",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EnumTypePayment> typesPayments;

    private BigDecimal cashAmount;
    private BigDecimal cashlessAmount;
    private BigDecimal creditAmount;

    @OneToMany(mappedBy = "paymentTransaction",cascade = CascadeType.ALL)
    private List<EnumMoneyLocation> locations;
}
