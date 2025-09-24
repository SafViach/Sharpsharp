package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.CategorySubcategory;
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
public class ResponseProductDTO {
    private UUID uuid;
    private String brand;
    private String model;
    private String characteristics;
    private int quantity;
    private String currency;
    private BigDecimal currencyRate;
    private String statusProduct;
//    private CategorySubcategory categorySubcategory;
    private String category;
    private String subcategory;
    private BigDecimal priceWithVat;
    private BigDecimal priceSelling;
    private String userAcceptedProduct;
    private String sku;
}
