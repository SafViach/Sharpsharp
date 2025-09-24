package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.CategoryMarginPercentageDTO;
import com.sharp.sharpshap.dto.CategoryPercentageOfSaleDTO;
import com.sharp.sharpshap.dto.CategoryNameDTO;
import com.sharp.sharpshap.dto.ResponseCategoryDTO;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);


    public List<ResponseCategoryDTO> getCategories() {
        logger.info("CategoryService: ---getCategories Получение всех категорий");
        List<Category> categoryList = categoryRepository.findAll();
        List<ResponseCategoryDTO> responseCategoriesDTO = categoryList.stream()
                .map(category -> toResponseCategoryDTO(category))
                .collect(Collectors.toList());

        return responseCategoriesDTO;
    }
    private ResponseCategoryDTO toResponseCategoryDTO(Category category){
        return new ResponseCategoryDTO(category.getId(), category.getName());
    }

    public ResponseCategoryDTO getCategoryDTOByUuid(UUID uuidCategory){
        Category category = getCategoryByUuid(uuidCategory);
        return toResponseCategoryDTO(category);
    }

    public Category getCategoryByUuid(UUID uuid) {
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


    public CategoryNameDTO createCategory(CategoryNameDTO categoryCreateDTO) {
        logger.warn("CategoryService: ---createCategory Внимание создание категории : " + categoryCreateDTO.getName());
        if (categoryRepository.findByNameIgnoreCase(categoryCreateDTO.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Категория с именем: " + categoryCreateDTO.getName() + " уже существует.");
        }
        Category newCategory = transFromDto(categoryCreateDTO);
        logger.warn("CategoryService: ---createCategory категория создана обращаем внимание на поля при дальнейшей работе" +
                " : " + newCategory.getName()
                + "/n " + newCategory.getPercentageOfSale()
                + "/n" + newCategory.getMarginPercentage());
        return transToDto(categoryRepository.save(newCategory));
    }

    public Category transFromDto(CategoryNameDTO categoryCreateDTO) {
        Category newCategory = new Category();
        newCategory.setName(categoryCreateDTO.getName());
        newCategory.setPercentageOfSale(new BigDecimal("13"));
        newCategory.setMarginPercentage(new BigDecimal("1.00"));
        return newCategory;
    }

    public CategoryNameDTO transToDto(Category category) {
        CategoryNameDTO newCategoryCreateDto = new CategoryNameDTO();
        newCategoryCreateDto.setName(category.getName());
        return newCategoryCreateDto;
    }

    public void deleteCategory(UUID uuidCategory) {
        Category category = getCategoryByUuid(uuidCategory);
        logger.info("CategoryService: ---deleteCategory Удаление: " + category.getName());
        boolean resultIsEmptyCategoryBySubcategory = isEmptyCategoryBySubcategory(category);
        logger.info("CategoryService: ---deleteCategory Проверили есть ли у категории подкатегории результат: " + !resultIsEmptyCategoryBySubcategory);
        boolean resultIsEmptyCategoryByProducts = isEmptyCategoryByProducts(category);
        logger.info("CategoryService: ---deleteCategory Проверили есть ли у категории продукты результат: " + !resultIsEmptyCategoryByProducts);

        if (resultIsEmptyCategoryBySubcategory && resultIsEmptyCategoryByProducts) {
            logger.info("CategoryService: ---deleteCategory Удаление прошло успешно категории: " + category.getName());
            categoryRepository.deleteById(uuidCategory);
        } else {
            throw new DeleteException("Удаление не возможно в связи с зависимыми ресурсами");
        }
    }

    private boolean isEmptyCategoryBySubcategory(Category category) {
        List<CategorySubcategory> categorySubcategories = category.getCategorySubcategories();
        List<Subcategory> subcategories = categorySubcategories.stream()
                .map(elem -> elem.getSubcategory())
                .collect(Collectors.toList());
        logger.info("CategoryService: ---isEmptyCategoryBySubcategory список подкатегорий : " + category.getName());
        subcategories.stream().forEach(elem -> logger.info(elem.getName()));
        if (subcategories.isEmpty()) {
            logger.info("Список подкатегорий в категории " + category.getName() + " пуст");
        }
        return subcategories.isEmpty();
    }

    private boolean isEmptyCategoryByProducts(Category category) {
        return !productService.checkCategoryForProducts(category);
    }

    public void updateName(CategoryNameDTO newCategoryNameDTO, UUID uuidCategory) {
        Category category = getCategoryByUuid(uuidCategory);
        category.setName(newCategoryNameDTO.getName());
        categoryRepository.save(category);
    }

    public void updatePercentageOfSale(CategoryPercentageOfSaleDTO percentageOfSaleDTO,
                                       UUID uuidCategory) {
        Category category = getCategoryByUuid(uuidCategory);
        category.setPercentageOfSale(percentageOfSaleDTO.getPercentageOfSale());
        categoryRepository.save(category);
    }

    public void updateMarginPercentage(CategoryMarginPercentageDTO marginPercentageDTO,
                                       UUID uuidCategory){
        Category category = getCategoryByUuid(uuidCategory);
        category.setMarginPercentage(marginPercentageDTO.getMarginPercentage());
        categoryRepository.save(category);
    }
}
