package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.CategorySubcategoryNotFoundException;
import com.sharp.sharpshap.exceptions.SubcategoryNotFoundException;
import com.sharp.sharpshap.repository.CategorySubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorySubcategoryService {
    private final CategorySubcategoryRepository categorySubcategoryRepository;
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategorySubcategoryService.class);

    public List<ResponseSubcategoryDTO> getSubcategories(UUID uuidCategory) {
        logger.info("CategorySubcategoryRepository: ---getSubcategories ищем Подкатегории по uuid категории");
        logger.info("CategorySubcategoryRepository: ---getSubcategories проверяем есть ли категория по uuid");
        categoryService.getCategoryById(uuidCategory);

        List<Subcategory> subcategories = categorySubcategoryRepository.findSubcategoriesByCategoryId(uuidCategory);


        List<ResponseSubcategoryDTO> subcategoriesDTO =
                subcategories.stream().map(ResponseSubcategoryDTO::to).collect(Collectors.toList());

        if (subcategories.isEmpty()) {
            new SubcategoryNotFoundException("CategorySubcategoryRepository: ---getSubcategories Нет подкатегорий в данной категории");
        }
        return subcategoriesDTO;
    }

    public CategorySubcategory getByCategoryAndSubcategory(Category category, Subcategory subcategory) {
        logger.info("CategorySubcategoryRepository: ---getByCategoryAndSubcategory");
        return subcategory == null
                ? categorySubcategoryRepository.findByCategoryAndSubcategoryIsNull(category).orElse(null)
                : categorySubcategoryRepository.findByCategoryAndSubcategory(category , subcategory).orElseThrow(
                        ()-> new CategorySubcategoryNotFoundException("Связь Категории/Подкатегориии не найдена"));
    }

    public CategorySubcategory getByCategory(Category category) {
        return categorySubcategoryRepository.findByCategory(category).orElse(null);
    }
}
