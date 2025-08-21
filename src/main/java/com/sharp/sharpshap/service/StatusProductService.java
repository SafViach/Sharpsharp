package com.sharp.sharpshap.service;

import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import com.sharp.sharpshap.exceptions.StatusProductNotFoundException;
import com.sharp.sharpshap.repository.StatusProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusProductService {
    private final StatusProductRepository statusProductRepository;

    public EnumStatusProduct findByName(String name){
        return statusProductRepository.findByStatus(name).orElseThrow(() ->
                new StatusProductNotFoundException("Статус по имени не найден"));
    }
}
