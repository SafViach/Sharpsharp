package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.service.CategorySubcategoryService;
import com.sharp.sharpshap.service.JwtService;
import com.sharp.sharpshap.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;
    private final static Logger logger = LoggerFactory.getLogger(CategorySubcategoryService.class);

    @GetMapping("/{uuidSubcategory}")
    public ResponseEntity<ResponseSubcategoryDTO> getSubcategory(@PathVariable UUID uuidSubcategory) {
        logger.info("SubcategoryController: ---getSubcategory");
        ResponseSubcategoryDTO responseSubcategoryDTO = subcategoryService.getSubcategoryDTOByUuid(uuidSubcategory);
        ResponseCookie cookie = ResponseCookie.from("uuidSubcategory", uuidSubcategory.toString())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JwtService.getRefreshExpirationMs())
                .domain("localhost")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseSubcategoryDTO);
    }

}
