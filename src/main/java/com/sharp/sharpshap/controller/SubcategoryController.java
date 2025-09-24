package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.service.CategorySubcategoryService;
import com.sharp.sharpshap.service.JwtService;
import com.sharp.sharpshap.service.SubcategoryService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("api/subcategories")
public class SubcategoryController {

    private final SubcategoryService subcategoryService;
    private final CategorySubcategoryService categorySubcategoryService;
    private final static Logger logger = LoggerFactory.getLogger(SubcategoryController.class);

    @GetMapping("/get-subcategories-by-uuid-category/for-add-product/{uuidCategory}")
    public ResponseEntity<List<ResponseSubcategoryDTO>> getAllSubcategoriesByCategoryUuid(@PathVariable(name = "uuidCategory") UUID uuidCategory) {
        logger.info("CategoryController: ---getAllSubcategoriesByCategoryUuid");
        List<ResponseSubcategoryDTO> responseSubcategoriesDTO = categorySubcategoryService.getSubcategories(uuidCategory);

        ResponseCookie cookieUuidCategory = ResponseCookie.from("uuidCategory", uuidCategory.toString())
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(43200)
                .build();

        logger.info("CategoryController:  ---getAllSubcategoriesByCategoryUuid Очищаем uuidSubcategory из cookie");
        ResponseCookie cookieUuidSubcategory = ResponseCookie.from("uuidSubcategory", "")
                .httpOnly(false)
                .secure(true)
                .path("/")
                .maxAge(0) // удаляет куку
                .build();

        logger.info("CategoryController: ---getAllSubcategoriesByCategoryUuid добавляем uuidCategory в cookie");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieUuidSubcategory.toString())
                .header(HttpHeaders.SET_COOKIE, cookieUuidCategory.toString()).body(responseSubcategoriesDTO);
    }

    @GetMapping("/{uuidSubcategory}")
    public ResponseEntity<ResponseSubcategoryDTO> getSubcategory(@PathVariable(name = "uuidSubcategory") UUID uuidSubcategory) {
        logger.info("SubcategoryController: ---getSubcategory Получение подкатегории по uuid и добавление в cookie");
        ResponseSubcategoryDTO responseSubcategoryDTO = subcategoryService.getSubcategoryDTOByUuid(uuidSubcategory);
        return ResponseEntity.ok().body(responseSubcategoryDTO);
    }

    @PostMapping("/createSubcategory/{uuidCategory}")
    public ResponseEntity<SubcategoryNameDTO> createSubcategory(@PathVariable(name = "uuidSubcategory") UUID uuidCategory,
                                                                @Valid @RequestBody SubcategoryNameDTO subcategoryCreateDTO) {
        logger.info("SubcategoryController: ---createSubcategory Создаём новую подкатегорию");
        SubcategoryNameDTO createDTO = subcategoryService.createSubcategory(subcategoryCreateDTO , uuidCategory);
        return ResponseEntity.ok().body(createDTO);
    }
    @DeleteMapping("/{uuidSubcategory}")
    public ResponseEntity deleteSubcategory(@CookieValue(name = "uuidCategory") UUID uuidCategory,
                                            @PathVariable(name = "uuidSubcategory") UUID uuidSubcategory){
        logger.info("SubcategoryController: ---deleteSubcategory Запускаем процесс удаления подкатегории");
        subcategoryService.deleteSubcategory(uuidCategory, uuidSubcategory);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{uuidSubcategory}")
    public ResponseEntity updateNameSubcategory(@PathVariable(name = "uuidSubcategory") UUID uuidSubcategory,
                                                @Valid @RequestBody SubcategoryNameDTO subcategoryNameDTO){
        subcategoryService.updateNameSubcategory(subcategoryNameDTO, uuidSubcategory);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/set-percentage-of-sale/{uuidSubcategory}")
    public ResponseEntity updatePercentageOfSale(@PathVariable(name = "uuidSubcategory") UUID uuidSubcategory,
                                                       @Valid @RequestBody SubcategoryPercentageOfSaleDTO percentageOfSaleDTO){
        subcategoryService.updatePercentageOfSale(percentageOfSaleDTO, uuidSubcategory);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/set-margin-percentage/{uuidSubcategory}")
    public ResponseEntity updateMarginPercentage(@PathVariable(name = "uuidSubcategory") UUID uuidSubcategory,
                                                 @Valid @RequestBody SubcategoryMarginPercentageDTO marginPercentageDTO){
        subcategoryService.updateMarginPercentage(marginPercentageDTO,uuidSubcategory);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/get-subcategories-by-uuid-category/for-update-product/{uuidCategory}")
    public ResponseEntity<List<ResponseSubcategoryDTO>> getAllSubcategoryByUuidCategory(@PathVariable(name = "uuidCategory") UUID uuidCategory){
       return ResponseEntity.ok().body(subcategoryService.getAllSubcategoryByUuidCategory(uuidCategory));
    }
}
