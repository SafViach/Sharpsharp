package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ResponseCatalogServiceDTO;
import com.sharp.sharpshap.dto.ServiceCatalogDTO;
import com.sharp.sharpshap.entity.ServiceCatalog;
import com.sharp.sharpshap.entity.TariffPlan;
import com.sharp.sharpshap.exceptions.ServiceCatalogException;
import com.sharp.sharpshap.repository.ServiceCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final ServiceCatalogRepository catalogRepository;
    public static final Logger logger = LoggerFactory.getLogger(CatalogService.class);

    private ServiceCatalog getServiceCatalogByUuid(UUID uuidServiceCatalog) {
        return catalogRepository.findById(uuidServiceCatalog).orElseThrow(() ->
                new ServiceCatalogException("Обьект услуги не найден в БД по uuid"));
    }

    public ResponseCatalogServiceDTO getServiceCatalogDTOByUuid(UUID uuidServiceCatalog){
        return ResponseCatalogServiceDTO.toDTO(getServiceCatalogByUuid(uuidServiceCatalog));
    }

    private List<ServiceCatalog> getServiceCatalogByKeyword(String keyword) {
        logger.info("CatalogService ---getServiceCatalogByKeyword поиск по keyword: " + keyword);

        String cleaner = keyword.trim().toLowerCase();
        String[] parts = cleaner.split("\\s+");
        logger.info("CatalogService ---getServiceCatalogByKeyword keyword содержет : " + parts.length + " слог");

        if (parts.length == 1) {
            return catalogRepository.findByKeyword(parts[0]);
        } else if (parts.length == 2) {
            return catalogRepository.findByKeywordTwoParts(parts[0], parts[1]);
        } else if (parts.length == 3) {
            return catalogRepository.findByKeywordThreeParts(parts[0], parts[1], parts[2]);
        } else if (parts.length == 4) {
            return catalogRepository.findByKeywordFourParts(parts[0], parts[1], parts[2], parts[3]);
        }
        return catalogRepository.findByKeywordFourParts(parts[0], parts[1], parts[2], parts[3]);
    }

    public List<ResponseCatalogServiceDTO> getServicesCatalogDTOByKeyword(String keyword) {
        logger.info("CatalogService ---getServicesCatalogDTOByKeyword keyword: " + keyword);
        return getServiceCatalogByKeyword(keyword).stream()
                .map(serviceCatalog -> ResponseCatalogServiceDTO.toDTO(serviceCatalog))
                .collect(Collectors.toList());
    }

    public List<ResponseCatalogServiceDTO> getAllServicesCatalogDTO() {
        logger.info("CatalogService ---getAllServicesCatalogDTO");
        return catalogRepository.findAll().stream()
                .map(serviceCatalog -> ResponseCatalogServiceDTO.toDTO(serviceCatalog))
                .collect(Collectors.toList());
    }

    @Transactional
    public void createServiceCatalog(ServiceCatalogDTO createCatalogDTO) {
        logger.info("CatalogService ---createServiceCatalog создание новой услуги : " +
                createCatalogDTO.getName() + " " + createCatalogDTO.getPrice());

        existsByName(createCatalogDTO.name, createCatalogDTO.price);

        ServiceCatalog serviceCatalog = new ServiceCatalog();

        serviceCatalog.setName(createCatalogDTO.name.trim());
        serviceCatalog.setPrice(createCatalogDTO.price);

        catalogRepository.save(serviceCatalog);
    }

    public void existsByName(String name, BigDecimal price) {
        logger.info("CatalogService ---existsByName проверяем есть ли данный обьект уже в БД ");
        if (catalogRepository.existsByNameAndPrice(name.trim(), price)) {
            throw new ServiceCatalogException("Услуга с именем: " + name +
                    " с данной суммой " + price + " присутствует в БД");
        }
    }


    public void deleteServiceCatalogByUuid(UUID uuidServiceCatalog) {
        logger.info("CatalogService ---deleteServiceCatalogByUuid");
        ServiceCatalog serviceCatalog = getServiceCatalogByUuid(uuidServiceCatalog);
        catalogRepository.delete(serviceCatalog);
    }

    @Transactional
    public void updateServiceCatalog(ServiceCatalogDTO updateServiceCatalogDTO, UUID uuidServiceCatalog) {
        ServiceCatalog serviceCatalog = getServiceCatalogByUuid(uuidServiceCatalog);
        logger.info("CatalogService ---updateServiceCatalog изменение услуги: " + serviceCatalog.getName() +
                " " + serviceCatalog.getPrice() + " на: " + updateServiceCatalogDTO.getName() + " " +
                updateServiceCatalogDTO.getPrice());
        serviceCatalog.setName(updateServiceCatalogDTO.getName());
        serviceCatalog.setPrice(updateServiceCatalogDTO.getPrice());

        catalogRepository.save(serviceCatalog);
    }


}
