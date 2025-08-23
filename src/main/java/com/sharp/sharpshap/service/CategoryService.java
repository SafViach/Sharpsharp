package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.CategoryCreateDTO;
import com.sharp.sharpshap.dto.ResponseCategoriesDTO;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.exceptions.DeleteException;
import com.sharp.sharpshap.exceptions.ResourceAlreadyExistsException;
import com.sharp.sharpshap.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);


    public ResponseCategoriesDTO getCategories() {
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
        if (categoryList.isEmpty()) {
            throw new RuntimeException("по  имени =" + prefix + " категории не найдены");
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


    public CategoryCreateDTO createCategory(CategoryCreateDTO categoryCreateDTO) {
        if (categoryRepository.findByNameIgnoreCase(categoryCreateDTO.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Категория с именем: " + categoryCreateDTO.getName() + " уже существует.");
        }
        Category newCategory = transFromDto(categoryCreateDTO);
        return transToDto(categoryRepository.save(newCategory));
    }

    public Category transFromDto(CategoryCreateDTO categoryCreateDTO) {
        Category newCategory = new Category();
        newCategory.setName(categoryCreateDTO.getName());
        newCategory.setCoefficientSale(new BigDecimal("1.0"));
        return newCategory;
    }

    public CategoryCreateDTO transToDto(Category category) {
        CategoryCreateDTO newCategoryCreateDto = new CategoryCreateDTO();
        newCategoryCreateDto.setName(category.getName());
        return newCategoryCreateDto;
    }
    public void deleteCategory(UUID uuidCategory){
        Category category = getCategoryById(uuidCategory);
        logger.info("CategoryService: ---deleteCategory Удаление: " + category.getName());
        boolean resultIsEmptyCategoryBySubcategory = isEmptyCategoryBySubcategory(category);
        logger.info("CategoryService: ---deleteCategory Проверили есть ли у категории подкатегории результат: " + !resultIsEmptyCategoryBySubcategory);
        boolean resultIsEmptyCategoryByProducts = isEmptyCategoryByProducts(category);
        logger.info("CategoryService: ---deleteCategory Проверили есть ли у категории продукты результат: " + !resultIsEmptyCategoryByProducts);

        if (resultIsEmptyCategoryBySubcategory && resultIsEmptyCategoryByProducts){
            logger.info("CategoryService: ---deleteCategory Удаление прошло успешно категории: "+ category.getName());
            categoryRepository.deleteById(uuidCategory);
        }else {
            throw new DeleteException("Удаление не возможно в связи с зависимыми ресурсами");
        }
    }
    private boolean isEmptyCategoryBySubcategory(Category category){
        List<CategorySubcategory> categorySubcategories = category.getCategorySubcategories();
        List<Subcategory> subcategories = categorySubcategories.stream()
                        .map(elem -> elem.getSubcategory())
                        .collect(Collectors.toList());
        logger.info("CategoryService: ---isEmptyCategoryBySubcategory список подкатегорий : "+ category.getName());
        subcategories.stream().forEach(elem -> logger.info(elem.getName()));
        if (subcategories.isEmpty()){
            logger.info("Список подкатегорий в категории " + category.getName() + " пуст");
        }
        return subcategories.isEmpty();
    }

    private boolean isEmptyCategoryByProducts(Category category){
        return !productService.checkCategoryForProducts(category);
    }
}
