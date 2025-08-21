package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ResponseSubcategoryDTO {
    private UUID uuid;
    private String name;

    public static ResponseSubcategoryDTO to (Subcategory sub){
        return new ResponseSubcategoryDTO(sub.getId() , sub.getName());
    }
    public static Subcategory from(ResponseSubcategoryDTO subcategoryDTO){
        return new Subcategory(subcategoryDTO.getUuid(),subcategoryDTO.getName(), BigDecimal.ZERO , null);
    }
}
