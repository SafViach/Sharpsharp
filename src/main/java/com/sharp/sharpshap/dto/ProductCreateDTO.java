package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductCreateDTO {

    private String brand;
    private String model;
    private String characteristics;

    @Min(value = 1, message = "товар принимается от единицы")
    private int quantity;

    @NotNull(message = "введите цену с НДС")
    @DecimalMin(value = "0.1", message = "цена должны быть не ниже 0.1")
    private BigDecimal priceWithVat;
    private UUID currencyId;
    private BigDecimal priceSelling;
    @NotNull(message = "введите курс по которому высчитывать цену товара")
    private BigDecimal rateCurrency;

    @AssertTrue(message = "Хотя бы одно поле (Брэнд, Модель или Характеристики) должно быть заполнено ")
    public boolean isValid() {
        return brand != null && !brand.isEmpty() ||
                model != null && !model.isEmpty() ||
                characteristics != null && !characteristics.isEmpty();
    }
}
