package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOfTheProductFoundDTO {
    private UUID uuid;
    private String brand;
    private String model;
    private String characteristics;
    private int quantity;

    public static ResponseOfTheProductFoundDTO toResponseOfTheProductFoundDTO(Product product){
        return new ResponseOfTheProductFoundDTO(product.getId(),
                Optional.ofNullable(product.getBrand()).orElse(" "),
                Optional.ofNullable(product.getModel()).orElse(" "),
                Optional.ofNullable(product.getCharacteristics()).orElse(" "),
                product.getQuantity());
    }
}
