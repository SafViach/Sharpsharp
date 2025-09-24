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
public class CategoryMarginPercentageDTO {
    //marginPercentage коэффициент надбавки маржи на товары по данной категории
    //Изменение доступны для всех
    @NotNull(message = "Введи коэффициент надбавки маржи на " +
            "товары по данной категории от 1.1 до 20.0")
    @DecimalMin(value = "1.1" ,message = "Коэффициент не может быть меньше 1.1")
    @DecimalMax(value ="20.0", message = "Коэффициент не может быть больше 20.0")
    private BigDecimal marginPercentage;
}
