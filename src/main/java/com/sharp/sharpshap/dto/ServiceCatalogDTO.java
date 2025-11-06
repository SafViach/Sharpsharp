package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ServiceCatalogDTO {
    @Size(min = 3, max = 100, message = "Наименование услуги должно быть от 3-x символов до 100")
    @NotBlank(message = "Опиши услугу: Имя/описание")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ1-90.\s]+$",
            message = "Допустимы только русские и английские буквы , цифры и точка так же пробелы " +
                    "(^[a-zA-Zа-яА-ЯёЁ1-90.\s]+$)")
    public String name;

    @DecimalMin(value = "0", message = "цена услуги не может быть отрицательной, можно просто 0")
    public BigDecimal price;
}
