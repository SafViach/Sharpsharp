package com.sharp.sharpshap.service;

import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        if (categoryList.isEmpty()) {
            throw new RuntimeException("Список категорий пуст");
        }
        return categoryList;
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException("Такой категории по id ="
                + id + "не найден"));
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
