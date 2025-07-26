package com.sharp.sharpshap.dto;

import com.sharp.sharpshap.entity.TradePoint;
import lombok.Data;

import java.util.List;
@Data
public class ResponseTradePointsDTO {
    private List<TradePoint> tradePoints;
}
