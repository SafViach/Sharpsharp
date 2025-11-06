package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.enums.EnumTypePayment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTypePaymentDTO {
    private UUID uuid;
    private String name;

    public static ResponseTypePaymentDTO toDTO (EnumTypePayment enumTypePayment){
        return new ResponseTypePaymentDTO(enumTypePayment.getId(), enumTypePayment.getType());
    }

}
