package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseCategoriesDTO;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);


    public ResponseCategoriesDTO getCategories(){
        logger.info("CategoryService: ---getCategories Получение всех категорий");
        ResponseCategoriesDTO responseCategoriesDTO = new ResponseCategoriesDTO();
        responseCategoriesDTO.setCategories(categoryRepository.findAll());
        if (categoryRepository.findAll().isEmpty()) {
            throw new CategoryNotFoundException("Список категорий пуст");
        }
        return responseCategoriesDTO;
    }

    public Category getCategoryById(UUID uuid) {
        logger.info("CategoryService: ---getCategoryById");
        return categoryRepository.findById(uuid).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));
    }

    public List<Category> getCategoriesByPrefix(String prefix) {
        List<Category> categoryList = categoryRepository.findByNameStartingWith(prefix);
        if (categoryList.isEmpty()){
            throw new RuntimeException("по  имени ="+ prefix + " категории не найдены");
        }
        return categoryList;
    }

//    public Category updateCategory(int id , Category category){
//        Category updateCategory = categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Такой категории по id ="
//                + id + "не найден"));
//        updateCategory.setName(category.getName());
//        updateCategory.setSubcategories(category.getSubcategories());
//        return categoryRepository.save(updateCategory);
//    }
//    public void deleteCategory(int id){
//        categoryRepository.deleteById(id);
//    }
}
