package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.CategoryCreateDTO;
import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.dto.ResponseCategoriesDTO;
import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.service.CategoryService;
import com.sharp.sharpshap.service.CategorySubcategoryService;
import com.sharp.sharpshap.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {
    public final CategoryService categoryService;
    public final CategorySubcategoryService categorySubcategoryService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @GetMapping("/{uuidCategory}")
    public ResponseEntity<List<ResponseSubcategoryDTO>> getAllSubcategoriesByCategoryUuid(@PathVariable UUID uuidCategory, HttpServletResponse response) {
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
    public ResponseEntity getCategories() {
        logger.info("CategoryController: ---getCategories отдаём лист категорий для выбора");
        ResponseCategoriesDTO responseCategoriesDTO = categoryService.getCategories();
        return ResponseEntity.ok().body(responseCategoriesDTO);
    }

    @PostMapping("/createCategory")
    public ResponseEntity<CategoryCreateDTO> createCategory(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO){
        CategoryCreateDTO createDTO = categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.ok().body(createDTO);
    }

    @DeleteMapping("/{uuidCategory}")
    public ResponseEntity deleteCategory(@PathVariable UUID uuidCategory){
        categoryService.deleteCategory(uuidCategory);
        return ResponseEntity.ok().build();

    }
}
