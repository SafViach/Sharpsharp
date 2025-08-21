package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseSubcategoryDTO;
import com.sharp.sharpshap.entity.Subcategory;
import com.sharp.sharpshap.exceptions.SubcategoryNotFoundException;
import com.sharp.sharpshap.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubcategoryService {
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


}
