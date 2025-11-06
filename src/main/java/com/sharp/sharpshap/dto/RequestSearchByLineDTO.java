package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestSearchByLineDTO {
    @NotBlank(message = "Поле для поиска не может быть пустым")
    @Size(min = 2, message = "Введите мин 2 символа для поиска товара")
    @Pattern(regexp = "^[1-90a-zA-Zа-яА-ЯёЁ\s-/]+$", message = "Допустимы только русские и английские буквы," +
            " цифры, так же пробелы,/ и тире(^[1-90a-zA-Zа-яА-ЯёЁ\s-/]+$)")
    private String lineSearch;
}
