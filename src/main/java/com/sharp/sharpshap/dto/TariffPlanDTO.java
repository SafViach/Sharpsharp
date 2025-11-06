package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffPlanDTO {
    @Size(min = 10 , max = 100, message = "Наименование тарифного плана должно быть от 10-ти символов до 100")
    @NotBlank(message = "Опиши тарифный план: Имя/описание/цена ")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ1-90.\s]+$",
            message = "Допустимы только русские и английские буквы , цифры и точка так же пробелы " +
                    "(^[a-zA-Zа-яА-ЯёЁ1-90.\s]+$)")
    public String name;

    @DecimalMin(value = "0" ,message = "цена тарифного плана не может быть отрицательной")
    public BigDecimal price;
}
