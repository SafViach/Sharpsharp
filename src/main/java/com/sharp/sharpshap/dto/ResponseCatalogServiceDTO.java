package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.ServiceCatalog;
import com.sharp.sharpshap.entity.TariffPlan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCatalogServiceDTO {
    private UUID uuid;
    private String name;
    private BigDecimal price;

    public static ResponseCatalogServiceDTO toDTO (ServiceCatalog serviceCatalog){
        return new ResponseCatalogServiceDTO(
                serviceCatalog.getId(),
                serviceCatalog.getName(),
                serviceCatalog.getPrice());
    }
}
