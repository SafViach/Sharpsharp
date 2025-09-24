package com.sharp.sharpshap.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseNameTradePointDTO {
    private UUID uuid;
    private String name;
}
