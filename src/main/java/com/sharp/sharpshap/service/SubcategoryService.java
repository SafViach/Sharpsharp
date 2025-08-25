package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.dto.SubcategoryPercentageOfSaleDTO;
import com.sharp.sharpshap.dto.SubcategoryNameDTO;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.exceptions.DeleteException;
import com.sharp.sharpshap.exceptions.ResourceAlreadyExistsException;
import com.sharp.sharpshap.exceptions.SubcategoryNotFoundException;
import com.sharp.sharpshap.repository.CategoryRepository;
import com.sharp.sharpshap.repository.SubcategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubcategoryService {
    private final CategorySubcategoryService categorySubcategoryService;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ProductService productService;
    private final static Logger logger = LoggerFactory.getLogger(SubcategoryService.class);

    public ResponseSubcategoryDTO getSubcategoryDTOByUuid(UUID uuidSubcategory) {
        Subcategory subcategory = getSubcategoryByUuid(uuidSubcategory);
        return ResponseSubcategoryDTO.to(subcategory);
    }

    public Subcategory getSubcategoryByUuid(UUID uuidSubcategory) {
        return subcategoryRepository.findById(uuidSubcategory).orElseThrow(() ->
                new SubcategoryNotFoundException("SubcategoryService: ---getSubcategoryByUuid " +
                        "подкатегория по UUID не найдена в БД"));
    }

    @Transactional
    public SubcategoryNameDTO createSubcategory(SubcategoryNameDTO subcategoryCreateDTO, UUID uuidCategory) {
        logger.warn("SubcategoryService: ---createSubcategory Внимание создание подкатегории : "
                + subcategoryCreateDTO.getName());

        if (subcategoryRepository.findByNameIgnoreCase(subcategoryCreateDTO.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Подкатегория по имени: " + subcategoryCreateDTO.getName() + " уже существует в базе");
        }
        Category category = categoryRepository.findById(uuidCategory).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));
        Subcategory newSubcategory = transFromDto(subcategoryCreateDTO);
        newSubcategory = subcategoryRepository.save(newSubcategory);
        categorySubcategoryService.save(category, newSubcategory);
        logger.warn("CategoryService: ---createCategory категория создана обращаем внимание на поля при дальнейшей работе" +
                " : " + newSubcategory.getName()
                + "/n " + newSubcategory.getPercentageOfSale()
                + "/n" + newSubcategory.getMarginPercentage());
        return transToDto(newSubcategory);
    }

    public void deleteSubcategory(UUID uuidCategory, UUID uuidSubcategory) {
        Subcategory subcategory = getSubcategoryByUuid(uuidSubcategory);
        Category category = categoryRepository.findById(uuidCategory).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));

        boolean resultCheckSubcategoryForProducts = productService.checkSubcategoryForProducts(subcategory);
        logger.info("SubcategoryService: ---deleteSubcategory Проверка подкатегории: " + subcategory.getName() + " на наличие связанных продуктов");
        logger.info("SubcategoryService: ---deleteSubcategory результат " + resultCheckSubcategoryForProducts);
        if (!resultCheckSubcategoryForProducts) {
            CategorySubcategory categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category ,subcategory);
            categorySubcategoryService.deleteCategorySubcategory(categorySubcategory);
            subcategoryRepository.delete(subcategory);
            logger.info("SubcategoryService: ---deleteSubcategory подкатегория удалена");
        } else {
            throw new DeleteException("Удаление не возможно в связи с зависимыми ресурсами");
        }

    }


    public SubcategoryNameDTO transToDto(Subcategory subcategory) {
        SubcategoryNameDTO subcategoryCreateDTO = new SubcategoryNameDTO();
        subcategoryCreateDTO.setName(subcategory.getName());
        return subcategoryCreateDTO;
    }

    public Subcategory transFromDto(SubcategoryNameDTO subcategoryCreateDTO) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryCreateDTO.getName());
        subcategory.setPercentageOfSale(new BigDecimal("13"));
        subcategory.setMarginPercentage(new BigDecimal("1.00"));
        return subcategory;
    }

    public void updateNameSubcategory(SubcategoryNameDTO subcategoryNameDTO, UUID uuidSubcategory){
        Subcategory subcategory = getSubcategoryByUuid(uuidSubcategory);
        subcategory.setName(subcategoryNameDTO.getName());
        subcategoryRepository.save(subcategory);
    }
    public void updatePercentageOfSale(SubcategoryPercentageOfSaleDTO percentageOfSaleDTO,
                                       UUID uuidSubcategory){
        Subcategory subcategory = getSubcategoryByUuid(uuidSubcategory);
        subcategory.setPercentageOfSale(percentageOfSaleDTO.getPercentageOfSale());
        subcategoryRepository.save(subcategory);
    }


}
