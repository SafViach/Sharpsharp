package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


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
                                        @CookieValue(name = "uuidSubcategory", required = false) UUID uuidSubcategory) {
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
    public ResponseEntity<ResponseProductSlice> getProductsByUuidTradePoint(@CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
                                                                            @RequestParam(required = false) UUID uuidProductAfter,
                                                                            @RequestParam(defaultValue = "2") int pageSize) {
        ResponseProductSlice sliceProducts = productService.getProductsByUuidTradePoint(
                uuidTradePoint,
                uuidProductAfter,
                pageSize);

        return ResponseEntity.ok().body(sliceProducts);
    }

    @GetMapping("/no-assignment-tp/{uuidTradePoint}")
    public ResponseEntity<ResponseProductSlice> getProductsByUuidTradePointNoAssignmentTradePointForUser(@PathVariable(name = "uuidTradePoint") UUID uuidTradePoint,
                                                                                                         @RequestParam(required = false) UUID uuidProductAfter,
                                                                                                         @RequestParam(defaultValue = "2") int pageSize) {
        ResponseProductSlice sliceProducts = productService.getProductsByUuidTradePointNoAssignmentTradePointForUser(
                uuidTradePoint, uuidProductAfter, pageSize);

        return ResponseEntity.ok().body(sliceProducts);

    }

    @GetMapping("/search")
    public ResponseEntity<ResponseProductSlice> searchProductByLine(
            @Valid @RequestBody RequestSearchByLineDTO requestSearchByLineDTO,
            @CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(required = false) UUID uuidProductAfter) {
        logger.info("ProductController: ---searchProductByLine поиск продуктов по подстраке :"
                + requestSearchByLineDTO.getLineSearch());
        ResponseProductSlice sliceProducts = productService.searchByLine(
                requestSearchByLineDTO,
                uuidTradePoint,
                uuidProductAfter,
                pageSize);

        return ResponseEntity.ok().body(sliceProducts);
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{uuidProduct}")
    public ResponseEntity deleteProduct (@PathVariable(name = "uuidProduct") UUID uuidProduct){
        logger.info("ProductController: ---deleteProduct удаление продукта администратором");
        productService.deleteProduct(uuidProduct);
        return ResponseEntity.ok().build();
    }
}
