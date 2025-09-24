package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseSubcategoryDTO {
    private UUID uuid;
    private String name;
}
