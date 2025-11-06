package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleRepository saleRepository;

}
