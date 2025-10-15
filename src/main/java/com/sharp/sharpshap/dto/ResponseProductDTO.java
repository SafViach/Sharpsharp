package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
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
    private final static Logger logger = LoggerFactory.getLogger(ResponseProductChangeRequestDTO.class);

    public static ResponseProductDTO toInResponseProductDTO(Product product) {
        logger.info("ResponseProductDTO: ---toInResponseProductDTO :  product преобразуем в  ResponseProductDTO");
        return new ResponseProductDTO(
                product.getId(),
                Optional.ofNullable(product.getBrand()).orElse(""),
                Optional.ofNullable(product.getModel()).orElse(""),
                Optional.ofNullable(product.getCharacteristics()).orElse(""),
                product.getQuantity(),
                Optional.ofNullable(product.getCurrency())
                        .map(EnumCurrency::getDescription)
                        .orElse(""),
                product.getCurrencyRate(),
                Optional.ofNullable(product.getStatusProduct())
                        .map(EnumStatusProduct::getStatus)
                        .orElse(""),
                Optional.ofNullable(product.getCategorySubcategory())
                        .map(CategorySubcategory::getCategory)
                        .map(Category::getName)
                        .orElse(""),
                Optional.ofNullable(product.getCategorySubcategory())
                        .map(CategorySubcategory::getSubcategory)
                        .map(Subcategory::getName)
                        .orElse(""),
                product.getPriceWithVat(),
                product.getPriceSelling(),
                Optional.ofNullable(product.getUserAcceptedProduct())
                        .map(user ->
                                Optional.ofNullable(user.getFirstName()).orElse("") + " " +
                                        Optional.ofNullable(user.getLastName()).orElse("")
                        )
                        .orElse(""),
                product.getSku());
    }
}
