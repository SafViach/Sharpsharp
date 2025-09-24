package com.sharp.sharpshap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseApplicationsDTO {
    private List<ResponseProductChangeRequestDTO> applicationsProductChangeRequestDTOS;
    private List<ResponseProductDTO> applicationsNewProductDTOS;


}
