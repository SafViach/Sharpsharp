package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.service.CategoryService;
import com.sharp.sharpshap.service.CategorySubcategoryService;
import com.sharp.sharpshap.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    public final CategoryService categoryService;
    public final CategorySubcategoryService categorySubcategoryService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @GetMapping("/{uuidCategory}")
    public ResponseEntity<List<ResponseSubcategoryDTO>> getAllSubcategoriesByCategoryUuid(
            @PathVariable(name = "uuidCategory") UUID uuidCategory) {
        logger.info("CategoryController: ---getAllSubcategoriesByCategoryUuid");
        List<ResponseSubcategoryDTO> responseSubcategoriesDTO = categorySubcategoryService.getSubcategories(uuidCategory);

        ResponseCookie cookie = ResponseCookie.from("uuidCategory", uuidCategory.toString())
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(43200)
                .build();

        logger.info("CategoryController: ---getAllSubcategoriesByCategoryUuid добавляем uuidCategory в cookie");

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseSubcategoriesDTO);
    }

    @GetMapping()
    public ResponseEntity<ResponseCategoriesDTO> getCategories() {
        logger.info("CategoryController: ---getCategories отдаём лист категорий для выбора");
        ResponseCategoriesDTO responseCategoriesDTO = categoryService.getCategories();
        return ResponseEntity.ok().body(responseCategoriesDTO);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryNameDTO> createCategory(@Valid @RequestBody CategoryNameDTO categoryCreateDTO){
        CategoryNameDTO createDTO = categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.ok().body(createDTO);
    }

    @DeleteMapping("/{uuidCategory}")
    public ResponseEntity deleteCategory(@PathVariable(name = "uuidCategory") UUID uuidCategory){
        categoryService.deleteCategory(uuidCategory);
        return ResponseEntity.ok().build();

    }
    @PatchMapping("/{uuidCategory}")
    public ResponseEntity updateNameCategory(@PathVariable(name = "uuidCategory") UUID uuidCategory,
                                             @Valid @RequestBody CategoryNameDTO newCategoryName){
        categoryService.updateName(newCategoryName , uuidCategory);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/coefficient-set/{uuidCategory}")
    public ResponseEntity updatePercentageOfSale(@PathVariable(name = "uuidCategory") UUID uuidCategory,
                                                    @Valid @RequestBody CategoryPercentageOfSaleDTO percentageOfSaleDTO){
        categoryService.updatePercentageOfSale(percentageOfSaleDTO,uuidCategory);
        return ResponseEntity.ok().build();
    }

}
