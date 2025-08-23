package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.dto.SubcategoryCreateDTO;
import com.sharp.sharpshap.entity.Category;
import com.sharp.sharpshap.entity.CategorySubcategory;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
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
    private final static Logger logger = LoggerFactory.getLogger(SubcategoryService.class);

    public ResponseSubcategoryDTO getSubcategoryDTOByUuid(UUID uuidSubcategory){
       Subcategory subcategory = getSubcategoryByUuid(uuidSubcategory);
      return ResponseSubcategoryDTO.to(subcategory);
    }
    public Subcategory getSubcategoryByUuid(UUID uuidSubcategory){
        return subcategoryRepository.findById(uuidSubcategory).orElseThrow(() ->
                new SubcategoryNotFoundException("SubcategoryService: ---getSubcategoryByUuid " +
                        "подкатегория по UUID не найдена в БД"));
    }

    @Transactional
    public SubcategoryCreateDTO createSubcategory(SubcategoryCreateDTO subcategoryCreateDTO , UUID uuidCategory){
        if(subcategoryRepository.findByNameIgnoreCase(subcategoryCreateDTO.getName()).isPresent()){
            throw new ResourceAlreadyExistsException("Подкатегория по имени: " + subcategoryCreateDTO.getName() + " уже существует в базе");
        }
        Category category = categoryRepository.findById(uuidCategory).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));
        Subcategory newSubcategory = transFromDto(subcategoryCreateDTO);
        newSubcategory = subcategoryRepository.save(newSubcategory);
        categorySubcategoryService.save(category , newSubcategory);
        return transToDto(newSubcategory);
    }



    public SubcategoryCreateDTO transToDto(Subcategory subcategory){
        SubcategoryCreateDTO subcategoryCreateDTO = new SubcategoryCreateDTO();
        subcategoryCreateDTO.setName(subcategory.getName());
        return subcategoryCreateDTO;
    }
    public Subcategory transFromDto(SubcategoryCreateDTO subcategoryCreateDTO){
        Subcategory subcategory = new Subcategory();
        subcategory.setName(subcategoryCreateDTO.getName());
        subcategory.setCoefficientSales(new BigDecimal("1.0"));
        return subcategory;
    }


}
