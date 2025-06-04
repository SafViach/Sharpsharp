package com.sharp.sharpshap.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
public class TradePoint{

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String address;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal moneyInBox;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal moneyInTheCashRegister;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal sumFinishOffTheMoney;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal totalLifeAndLaptop;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal totalLife;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal totalLaptop;

}
