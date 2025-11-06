package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.RequestSearchByLineDTO;
import com.sharp.sharpshap.dto.ResponseResultSearchDTO;
import com.sharp.sharpshap.service.DeterminationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/determinant")
public class DeterminantController {
    private final DeterminationService determinationService;
    private final static Logger logger = LoggerFactory.getLogger(DeterminantController.class);

    @GetMapping
    public ResponseEntity<ResponseResultSearchDTO> getProductsOrServiceByKeyword(
            @Valid @RequestBody RequestSearchByLineDTO requestSearchByLineDTO,
            @CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(required = false) UUID uuidProductAfter) {

        logger.info("SaleController: ---getProductsOrServiceByKeyword ");

        return ResponseEntity.ok().body(determinationService.getResultSearch(requestSearchByLineDTO,
                uuidTradePoint,
                uuidProductAfter,
                pageSize));
    }


}
