package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.repository.ProductRepository;
import com.sharp.sharpshap.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;


    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO,
                                                  @RequestParam UUID categoryUUID,
                                                  @RequestParam UUID currencyUUID,
                                                  @RequestParam(required = false) UUID subcategoryUUID,
                                                  BindingResult result){
        if (result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", ")));
        }
        productService.createdProduct(productCreateDTO, categoryUUID, subcategoryUUID, currencyUUID);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Custom-Message", "Продукт отправлен на проверку")
                .body("");
    }
}
