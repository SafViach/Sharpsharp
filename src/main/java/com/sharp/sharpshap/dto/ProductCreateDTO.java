package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductCreateDTO {

    private String brand;
    private String model;
    private String characteristics;

    @Min(value = 1 , message = "товар принимается от единицы")
    private int quantity;

//    @NotBlank(message = "Выберите курс по которому прибыл товар")
//    private EnumCurrency currency;

    @NotNull(message = "введите цену с НДС")
    @Min(value = 1, message = "цена должны быть не ниже 0.1")
    private BigDecimal priceWithVat;

    //private CategorySubcategory categorySubcategory;

    @AssertTrue(message = "Хотя бы одно поле (brand, model или characteristics) должно быть заполнено ")
    public boolean isValid(){
        return brand != null && !brand.isEmpty() ||
                model !=null && !model.isEmpty() ||
                characteristics != null && !characteristics.isEmpty();
    }
}
