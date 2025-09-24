package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductChangeRequestDTO {
    private UUID uuid;
    private Product product;
    private String brand;
    private String model;
    private String characteristics;
    private int quantity;
    private EnumCurrency currency;
    private BigDecimal currencyRate;
    private BigDecimal priceWithVat;
    private BigDecimal priceSelling;
    private EnumStatusProduct statusProduct;
    private CategorySubcategory categorySubcategory;
    private String sku;
    private User user;
    private TradePoint tradePoint;

}
