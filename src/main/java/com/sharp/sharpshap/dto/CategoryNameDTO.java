package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryNameDTO {
    @Size(min = 3 , max = 15, message = "Наименование категории далжно состоять от 3-x символов до 15-ти")
    @NotBlank(message = "Наименование категории не может быть пустым")
    @Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\s]+$", message = "Допустимы только русские и английские буквы так же пробелы (^[a-zA-Zа-яА-ЯёЁ\s]+$)")
    private String name;
}
