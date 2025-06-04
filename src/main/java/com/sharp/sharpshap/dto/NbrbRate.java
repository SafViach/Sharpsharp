package com.sharp.sharpshap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NbrbRate {
    @JsonProperty("Cur_ID")
    private int curId;
    
    @JsonProperty("Date")
    private String date;
    
    @JsonProperty("Cur_Abbreviation")
    private String curAbbreviation;
    
    @JsonProperty("Cur_Scale")
    private int curScale;
    
    @JsonProperty("Cur_Name")
    private String curName;
    
    @JsonProperty("Cur_OfficialRate")
    private BigDecimal curOfficialRate;
}
