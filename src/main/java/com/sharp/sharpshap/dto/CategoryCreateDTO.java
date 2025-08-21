package com.sharp.sharpshap.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateDTO {
    @Size(min = 2 , max = 15, message = "Наименование категории далжно состояти от 2-ч символов до 15-ти")
    @NotBlank(message = "Наименование категории не может быть пустым")
    private String name;
}
