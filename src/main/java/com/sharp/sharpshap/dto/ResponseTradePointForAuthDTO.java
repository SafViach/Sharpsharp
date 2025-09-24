package com.sharp.sharpshap.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTradePointForAuthDTO {
    private UUID uuid;
    private String name;
    private String address;
    private BigDecimal moneyInBox;
    private BigDecimal moneyInTheCashRegister;
    private BigDecimal sumFinishOffTheMoney;
}
