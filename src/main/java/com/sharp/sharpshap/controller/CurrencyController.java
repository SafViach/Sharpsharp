package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.CurrencyResponseDTO;
import com.sharp.sharpshap.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyResponseDTO>> getAllCurrency(){
        List<CurrencyResponseDTO> currencyResponseDTO = currencyService.getAllCurrencyDTO();
        return ResponseEntity.ok().body(currencyResponseDTO);
    }
    @GetMapping("/{uuidCurrency}")
    public ResponseEntity<CurrencyResponseDTO> getByUuidCurrency(@PathVariable UUID uuidCurrency){
        CurrencyResponseDTO responseDTO = currencyService.getDtoById(uuidCurrency);
        return ResponseEntity.ok().body(responseDTO);
    }

}
