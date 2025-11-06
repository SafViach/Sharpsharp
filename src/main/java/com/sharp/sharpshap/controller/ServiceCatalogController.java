package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ResponseCatalogServiceDTO;
import com.sharp.sharpshap.dto.ServiceCatalogDTO;
import com.sharp.sharpshap.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/serviceCatalog")
public class ServiceCatalogController {
    private final CatalogService catalogService;
    private final static Logger logger = LoggerFactory.getLogger(ServiceCatalogController.class);

    @GetMapping("all")
    public ResponseEntity<List<ResponseCatalogServiceDTO>> getAllServicesCatalog(){
        logger.info("ServiceCatalogController: ---getAllServicesCatalog получение всех услуг");
        return ResponseEntity.ok().body(catalogService.getAllServicesCatalogDTO());
    }

    @GetMapping("/{uuidServiceCatalog}")
    public ResponseEntity<ResponseCatalogServiceDTO> getServiceCatalogByUuid(
            @PathVariable(name = "uuidServiceCatalog") UUID uuidServiceCatalog){
        logger.info("ServiceCatalogController: ---getServiceCatalogByUuid получение услуги по uuid");
        return ResponseEntity.ok().body(catalogService.getServiceCatalogDTOByUuid(uuidServiceCatalog));
    }

    @PostMapping
    public ResponseEntity createServiceCatalog(@Valid @RequestBody ServiceCatalogDTO createServiceCatalogDTO) {
        logger.info("ServiceCatalogController ---createServiceCatalog создание новой услуги");
        catalogService.createServiceCatalog(createServiceCatalogDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{uuidServiceCatalog}")
    public ResponseEntity updateServiceCatalog(@PathVariable(name = "uuidServiceCatalog") UUID uuidServiceCatalog,
                                           @Valid @RequestBody ServiceCatalogDTO serviceCatalogDTO) {
        logger.info("ServiceCatalogController ---updateTariffPlan изменение тарифного плана по uuid");

        catalogService.updateServiceCatalog(serviceCatalogDTO, uuidServiceCatalog);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{uuidServiceCatalog}")
    public ResponseEntity deleteServiceCatalog(@PathVariable(name = "uuidServiceCatalog") UUID uuidServiceCatalog){
        logger.info("ServiceCatalogController ---deleteTariffPlan удаление тарифного плана по uuid");

        catalogService.deleteServiceCatalogByUuid(uuidServiceCatalog);

        return ResponseEntity.ok().build();
    }

}
