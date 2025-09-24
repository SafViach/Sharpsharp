package com.sharp.sharpshap.service;

import com.sharp.sharpshap.entity.CategorySubcategory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilGenerateSKUService {
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    public String generationFieldSku(CategorySubcategory categorySubcategory, String brand, String model, String characteristics) {
        String nameSubcategory = categorySubcategory.getSubcategory() != null ? categorySubcategory.getSubcategory().getName() :
                "null";
        String nameCategory = categorySubcategory.getCategory().getName();
        String resultSku = String.join("-", nameCategory, nameSubcategory, brand, model, characteristics).toUpperCase();
        logger.info("UtilGenerateSKU: ---generationFieldSku создаём поле sku : " + resultSku);
        return resultSku;
    }
}
