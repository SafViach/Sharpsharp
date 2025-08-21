package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.dto.ResponseCategoriesDTO;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.service.CategoryService;
import com.sharp.sharpshap.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);


//    @PostMapping
//    public ResponseEntity<Object> addProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO,
//                                             @RequestParam UUID categoryUUID,
//                                             @RequestParam UUID currencyUUID,
//                                             @RequestParam(required = false) UUID subcategoryUUID,
//                                             BindingResult result) {
//        if (result.hasErrors()) {
//            return ResponseEntity.badRequest().body(result.getFieldErrors().stream()
//                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                    .collect(Collectors.joining(", ")));
//        }
//        productService.createdProduct(productCreateDTO, categoryUUID, subcategoryUUID, currencyUUID);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .header("Custom-Message", "Продукт отправлен на проверку")
//                .body("");
//    }


    @PostMapping
    public ResponseEntity createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO,
                                        @CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
                                        @RequestAttribute(name = "uuidUser") UUID uuidUser,
                                        @CookieValue(name = "uuidCategory") UUID uuidCategory,
                                        @CookieValue(name = "uuidSubcategory", required = false)
                                                UUID uuidSubcategory) {
        productService.createdProduct(productCreateDTO,
                uuidCategory,
                uuidSubcategory,
                uuidUser,
                uuidTradePoint);
        return ResponseEntity.ok().build();
    }


}
