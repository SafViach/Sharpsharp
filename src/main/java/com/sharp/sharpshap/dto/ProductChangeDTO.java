package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductChangeDTO {
    @NotNull(message = "Выберите категорию")
    private UUID uuidCategory;
    private UUID uuidSubcategory;
    private String brand;
    private String model;
    private String characteristics;

    @Min(value = 1, message = "товар принимается от единицы")
    private int quantity;

    @NotNull(message = "введите цену с НДС")
    @DecimalMin(value = "0.1", message = "цена должны быть не ниже 0.1")
    private BigDecimal priceWithVat;
    @NotNull(message = "введите курс по которому высчитывать цену товара")
    private BigDecimal rateCurrency;
    private UUID uuidCurrency;

    private BigDecimal priceSelling;


    @AssertTrue(message = "Хотя бы одно поле (Брэнд, Модель или Характеристики) должно быть заполнено ")
    public boolean isValid() {
        return brand != null && !brand.isEmpty() ||
                model != null && !model.isEmpty() ||
                characteristics != null && !characteristics.isEmpty();
    }

}
