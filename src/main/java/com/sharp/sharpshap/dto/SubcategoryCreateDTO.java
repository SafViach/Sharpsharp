package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryCreateDTO {
    @Size(min = 3 , max = 30, message = "Наименование подкатегории далжно состоять от 3-x символов до 30-ти")
    @NotBlank(message = "Наименование подкатегории не может быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\s-]+$", message = "Допустимы только русские и английские буквы так же пробелы и тире(^[a-zA-Zа-яА-ЯёЁ\s-]+$)")
    private String name;
}
