package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.TradePoint;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private List<TradePoint> tradePoints;
    private String accessToken;
    private String message;
}
