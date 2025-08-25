package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryPercentageOfSaleDTO {
    @NotNull(message = "Введи процент выплаты работнику за проданные " +
            "товары по данной подкатегории от 1-го до 100")
    @DecimalMin(value = "1" ,message = "Коэффициент не может быть меньше 1")
    @DecimalMax(value ="100", message = "Коэффициент не может быть больше 100")
    private BigDecimal percentageOfSale;
}
