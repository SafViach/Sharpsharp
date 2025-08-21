package com.sharp.sharpshap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponseDTO {
    private UUID id;
    private String description;
    private BigDecimal rate;
    private LocalDateTime lastUpdate;
}
