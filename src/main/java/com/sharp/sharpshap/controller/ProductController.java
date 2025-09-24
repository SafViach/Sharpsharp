package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @PostMapping
    public ResponseEntity createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO,
                                        @CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
                                        @RequestAttribute(name = "uuidUser") UUID uuidUser,
                                        @CookieValue(name = "uuidCategory") UUID uuidCategory,
                                        @CookieValue(name = "uuidSubcategory", required = false)
                                                UUID uuidSubcategory) {
        logger.info("ProductController: ---createProduct Создание продукта");
        logger.info("ProductController: ---createProduct полученные данные: productCreateDTO " + productCreateDTO);
        productService.createdProduct(productCreateDTO,
                uuidCategory,
                uuidSubcategory,
                uuidUser,
                uuidTradePoint);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getByUuidTradePoint")
    public ResponseEntity<List<ResponseProductDTO>> getProductsByUuidTradePoint(@CookieValue(name = "uuidTradePoint") UUID uuidTradePoint) {
        List<ResponseProductDTO> products = productService.getProductsByUuidTradePoints(uuidTradePoint);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/no-assignment-tp/{uuidTradePoint}")
    public ResponseEntity getProductsByUuidTradePointNoAssignmentTradePointForUser(@PathVariable(name = "uuidTradePoint") UUID uuidTradePoint) {
        return ResponseEntity.ok().body(productService.getProductsByUuidTradePointNoAssignmentTradePointForUser(uuidTradePoint));

    }

    @GetMapping("/search")
    public ResponseEntity<List<ResponseOfTheProductFoundDTO>> searchProductByLine(@Valid @RequestBody RequestProductSearchDTO productSearchDTO,
                                                                                  @CookieValue(name = "uuidTradePoint") UUID uuidTradePoint) {
        logger.info("ProductController: ---searchProductByLine поиск продуктов по подстраке :" + productSearchDTO.getLineSearch());
        return ResponseEntity.ok().body(productService.searchByLine(productSearchDTO, uuidTradePoint));
    }

    @GetMapping("/{uuidProduct}")
    public ResponseEntity<ResponseProductDTO> getProductByUuid(@PathVariable(name = "uuidProduct") UUID uuidProduct) {
        logger.info("ProductController: ---getProductByUuid получение продукта по uuid");
        return ResponseEntity.ok().body(productService.getResponseProductDTOByUuid(uuidProduct));
    }

    @PatchMapping("/product-change/{uuidProduct}")
    public ResponseEntity productChange(@PathVariable(name = "uuidProduct") UUID uuidProduct,
                                        @RequestAttribute(name = "uuidUser") UUID uuidUser,
                                        @RequestAttribute(name = "uuidTradePoint") UUID uuidTradePoint,
                                        @RequestBody ProductChangeDTO productChangeDTO) {
        logger.info("ProductController: ---productChange изменения продукта");
        productService.productChange(uuidProduct, uuidUser, uuidTradePoint, productChangeDTO);
        return ResponseEntity.ok().build();
    }
}
