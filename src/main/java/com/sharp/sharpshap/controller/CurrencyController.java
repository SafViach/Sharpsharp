package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.CurrencyResponseDTO;
import com.sharp.sharpshap.service.CategorySubcategoryService;
import com.sharp.sharpshap.service.CurrencyService;
import com.sharp.sharpshap.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final static Logger logger = LoggerFactory.getLogger(CurrencyController.class);

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
