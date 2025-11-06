package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ResponseTypePaymentDTO;
import com.sharp.sharpshap.service.TypePaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/typePayment")
@RequiredArgsConstructor
public class TypePaymentController {
    private final TypePaymentService typePaymentService;
    public static final Logger logger = LoggerFactory.getLogger(TypePaymentController.class);

    @GetMapping
    public ResponseEntity<List<ResponseTypePaymentDTO>> getAllTypePayment(){
        logger.info("TypePaymentController: ---getAllTypePayment");
       return ResponseEntity.ok().body(typePaymentService.getAllTypePaymentsDTO());
    }
}
