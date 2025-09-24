package com.sharp.sharpshap.dto;

import lombok.*;

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
}
