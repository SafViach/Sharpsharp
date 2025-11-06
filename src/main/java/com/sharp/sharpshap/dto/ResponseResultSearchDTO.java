package com.sharp.sharpshap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResultSearchDTO {
    private ResponseProductSlice productFoundDTO;
    private List<ResponseCatalogServiceDTO> catalogServiceDTOS;
    private List<ResponseTariffPlanDTO> tariffPlanDTOS;

    public ResponseResultSearchDTO responseResultSearchDTO(ResponseProductSlice productSlice,
                                                           List<ResponseCatalogServiceDTO> catalogServiceDTOS,
                                                           List<ResponseTariffPlanDTO> responseTariffPlanDTOS){
        return new ResponseResultSearchDTO(productSlice, catalogServiceDTOS, tariffPlanDTOS);

    }
}
