package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.service.ProductChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/productChangeRequest")
public class ProductChangeRequestController{
    private ProductChangeRequestService productChangeRequestService;
    public static final Logger logger = LoggerFactory.getLogger(ProductChangeRequestController.class);


//    //заявки
//    //Получение продуктов статуса: PENDING_APPROVAL(ожидание подтверждения)
//    //C информацией продкта с новыми и сстарыми полями , а так же кто производит изменения товара
//    @GetMapping
//    public ResponseEntity<> createProductChangeRequest()
}
