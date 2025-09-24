package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.exceptions.CategorySubcategoryNotFoundException;
import com.sharp.sharpshap.exceptions.CreateProductException;
import com.sharp.sharpshap.exceptions.ResourceAlreadyExistsException;
import com.sharp.sharpshap.repository.CategoryRepository;
import com.sharp.sharpshap.repository.CategorySubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorySubcategoryService {
    private final CategorySubcategoryRepository categorySubcategoryRepository;
    private final CategoryRepository categoryRepository;
    private final Logger logger = LoggerFactory.getLogger(CategorySubcategoryService.class);

    public List<ResponseSubcategoryDTO> getSubcategories(UUID uuidCategory) {
        logger.info("CategorySubcategoryRepository: ---getSubcategories ищем Подкатегории по uuid категории");
        logger.info("CategorySubcategoryRepository: ---getSubcategories проверяем есть ли категория по uuid");
        categoryRepository.findById(uuidCategory).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));

        List<Subcategory> subcategories = categorySubcategoryRepository.findSubcategoriesByCategoryId(uuidCategory);


        List<ResponseSubcategoryDTO> subcategoriesDTO =
                subcategories.stream().map(subcategory -> new ResponseSubcategoryDTO(subcategory.getId(), subcategory.getName()))
                        .collect(Collectors.toList());

        return subcategoriesDTO;
    }

    public CategorySubcategory getById(UUID uuidCategorySubcategory) {
        return categorySubcategoryRepository.findById(uuidCategorySubcategory).orElseThrow(() ->
                new CategorySubcategoryNotFoundException("Связь категории и подкатегории по uuid не найдена в бд"));
    }

    public void deleteCategorySubcategory(CategorySubcategory categorySubcategory) {
        logger.info("CategorySubcategoryService: ---deleteCategorySubcategory Удаляем вявь " + categorySubcategory.getCategory().getName()
                + " c " + categorySubcategory.getSubcategory().getName());
        getById(categorySubcategory.getId());
        categorySubcategoryRepository.deleteById(categorySubcategory.getId());
        logger.info("Связь удалена");
    }

    public CategorySubcategory getByCategoryAndSubcategory(Category category, Subcategory subcategory) {
        if (category == null) {
            logger.error("CategorySubcategoryService : ---getByCategoryAndSubcategory category == null");
            throw new CategoryNotFoundException("CategorySubcategoryService : ---getByCategoryAndSubcategory нет Категории");
        }
        if (subcategory == null) {
            logger.info("CategorySubcategoryService : ---getByCategoryAndSubcategory  поиск Категории и Подкатегории " +
                    category.getName() + "subcategory == null");
            return categorySubcategoryRepository
                    .findByCategoryAndSubcategoryIsNull(category)
                    .orElseGet(() -> createCategorySubcategory(category, null));
        }
        return categorySubcategoryRepository
                .findByCategoryAndSubcategory(category, subcategory)
                .orElseGet(() -> createCategorySubcategory(category, subcategory));
    }

    public CategorySubcategory createCategorySubcategory(Category category, Subcategory subcategory){
        String nameSubcategory = subcategory != null ? subcategory.getName() : "null";

        logger.warn("CategorySubcategoryService : ---getByCategoryAndSubcategory создаём новую связь " + category.getName()
        + "-" + nameSubcategory);
        CategorySubcategory categorySubcategory = new CategorySubcategory();
        categorySubcategory.setCategory(category);
        categorySubcategory.setSubcategory(subcategory);
        return categorySubcategoryRepository.save(categorySubcategory);
    }

    public CategorySubcategory getByCategory(Category category) {
        return categorySubcategoryRepository.findByCategory(category).orElse(null);
    }


    public CategorySubcategory save(Category category, Subcategory subcategory) {
        if (checkForAvailability(category, subcategory)) {
            logger.info("CategorySubcategoryService: ---save Связь : " + category + " + " + subcategory + " уже существует в БД");
        }
        logger.info("CategorySubcategoryService: ---save Связь : " + category + " + " + subcategory + " создаем и сохраняем в БД");
        CategorySubcategory categorySubcategory = new CategorySubcategory();
        categorySubcategory.setCategory(category);
        categorySubcategory.setSubcategory(subcategory);
        return categorySubcategoryRepository.save(categorySubcategory);
    }

    private boolean checkForAvailability(Category category, Subcategory subcategory) {
        categoryRepository.findById(category.getId()).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));
        List<Subcategory> subcategories = categorySubcategoryRepository.findSubcategoriesByCategoryId(category.getId());
        if (subcategories.isEmpty()) {
            logger.info("У данной категории :" + category + " нет подкатегорий");
        }
        Optional<Subcategory> resultFindSubcategory = subcategories.stream()
                .filter(sub -> sub.getName().equalsIgnoreCase(subcategory.getName()))
                .findFirst();
        if (resultFindSubcategory.isPresent()) {
            logger.info("Подкатегория :" + subcategory + "найдена в категории : " + category);
            throw new ResourceAlreadyExistsException("Подкатегория :" + subcategory + "найдена в категории : " + category);
        } else {
            logger.info("Подкатегория :" + subcategory + " не найдена в категории : " + category);
            return false;
        }
    }
}
