package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseApplicationsDTO;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.dto.ResponseProductDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationForAdminService {
    private final ProductChangeRequestService productChangeRequestService;
    private final ProductService productService;
    public static final Logger logger = LoggerFactory.getLogger(ApplicationForAdminService.class);

    public List<ResponseProductChangeRequestDTO> getAllApplicationsProductChangeRequestDTO(){
        logger.info("ApplicationForAdminService: ---getAllApplicationProductChangeRequestDTO");
        return productChangeRequestService.getAllProductChangeDTO();
    }
    public List<ResponseProductDTO> getAllApplicationsAddNewProduct(){
        logger.info("ApplicationForAdminService: ---getAllApplicationsAddNewProduct");
        return productService.getNewProductsDTOForApplications();
    }
}
