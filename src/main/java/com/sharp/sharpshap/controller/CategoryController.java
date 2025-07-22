package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.error.ErrorResponse;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    public CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')&& hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> getAllCategory() {
        try {
            return ResponseEntity.ok().body(categoryService.getAllCategory());
        } catch (RuntimeException e) {
            return ErrorResponse.error(new CategoryNotFoundException("Categories not found"),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable UUID uuid) {
        return ResponseEntity.ok().body(categoryService.getCategoryById(uuid));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Object> updateCategory(@PathVariable int id,
//                                                 @Valid @RequestBody Category category,
//                                                 BindingResult result) {
//        try {
//            if (result.hasErrors()) {
//                String errors = result.getFieldErrors().stream()
//                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                        .collect(Collectors.joining(","));
//                return ResponseEntity.badRequest().body("Объект не валиден" + errors);
//            }
//            return ResponseEntity.ok().body(categoryService.updateCategory(id, category));
//        } catch (RuntimeException e) {
//            return ErrorResponse.error(e);
//        }
//    }

    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody Category category,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(","));
            return ResponseEntity.badRequest().body("Объект не валиден" + errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");

    }

//    @DeleteMapping
//    public ResponseEntity<Object> deleteCategory(@PathVariable int id) {
//        try {
//            categoryService.deleteCategory(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ErrorResponse.error(e);
//        }
//    }
//    @GetMapping("/search-by-prefix")
//    public
}
