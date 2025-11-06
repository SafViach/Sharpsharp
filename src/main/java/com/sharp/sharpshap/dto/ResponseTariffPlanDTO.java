package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.TariffPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseTariffPlanDTO {
    private UUID uuid;
    private String name;
    private BigDecimal price;

    public static ResponseTariffPlanDTO toDTO (TariffPlan tariffPlan){
        return new ResponseTariffPlanDTO(tariffPlan.getId(), tariffPlan.getName(), tariffPlan.getPrice());
    }
}

