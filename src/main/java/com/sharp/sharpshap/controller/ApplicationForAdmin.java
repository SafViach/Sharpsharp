package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.dto.ResponseApplicationsDTO;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.dto.ResponseProductDTO;
import com.sharp.sharpshap.service.ApplicationForAdminService;
import com.sharp.sharpshap.service.ProductChangeRequestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/application")
public class ApplicationForAdmin {
    private final ApplicationForAdminService applicationForAdminService;
    public static final Logger logger = LoggerFactory.getLogger(ApplicationForAdmin.class);

    @GetMapping
    public ResponseEntity<ResponseApplicationsDTO>getAllApplications(){
        logger.info("ApplicationForAdmin: ---getAllProductChangeRequestDTO");
        ResponseApplicationsDTO responseApplicationsDTO = new ResponseApplicationsDTO();

        responseApplicationsDTO.setApplicationsProductChangeRequestDTOS(
                applicationForAdminService.getAllApplicationsProductChangeRequestDTO());
        responseApplicationsDTO.setApplicationsNewProductDTOS(
                applicationForAdminService.getAllApplicationsAddNewProduct());

        return ResponseEntity.ok().body(responseApplicationsDTO);
    }
}
