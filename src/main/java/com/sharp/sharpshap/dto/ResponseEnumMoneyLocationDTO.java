package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.enums.EnumMoneyLocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseEnumMoneyLocationDTO {
    private UUID uuidMoneyLocation;
    private String path;

    public static ResponseEnumMoneyLocationDTO toDTO(EnumMoneyLocation moneyLocation){
        return new ResponseEnumMoneyLocationDTO(moneyLocation.getId(), moneyLocation.getPath());
    }
}
