package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPercentageOfSaleDTO {
    //percentageOfSale процент выплаты работнику за проданные товар данной категории
    //Изменение доступны только для директора
    @NotNull(message = "Введи процент выплаты работнику за проданные " +
            "товары по данной категории от 1-го до 100")
    @DecimalMin(value = "1" ,message = "Коэффициент не может быть меньше 1")
    @DecimalMax(value ="100", message = "Коэффициент не может быть больше 100")
    private BigDecimal percentageOfSale;
}
