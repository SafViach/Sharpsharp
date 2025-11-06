package com.sharp.sharpshap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSaleDTO {
    private String typeSale;
    private BigDecimal salePrice;
    private UUID uuidTypePayment;
    private UUID uuidMoneyLocation;
    private UUID uuidUser;
    private UUID uuidTradePoint;
    private UUID uuidProductOrService;
}
