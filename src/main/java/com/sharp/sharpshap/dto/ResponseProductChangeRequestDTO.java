package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import com.sharp.sharpshap.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProductChangeRequestDTO {
    private UUID uuid;
    private ResponseProductDTO product;
    private String brand;
    private String model;
    private String characteristics;
    private int quantity;
    private String nameCurrency;
    private BigDecimal currencyRate;
    private BigDecimal priceWithVat;
    private BigDecimal priceSelling;
    private String nameStatusProduct;
    private String sku;
    private String nameUser;
    private String nameTradePoint;

}
