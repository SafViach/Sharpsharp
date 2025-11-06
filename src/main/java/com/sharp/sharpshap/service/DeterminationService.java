package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeterminationService {
    private final ProductService productService;
    private final CatalogService catalogService;
    private final TariffPlanService tariffPlanService;
    private final Logger logger = LoggerFactory.getLogger(DeterminationService.class);

    public ResponseResultSearchDTO getResultSearch(RequestSearchByLineDTO requestSearchByLineDTO,
                                                   UUID uuidTradePoint,
                                                   UUID uuidProductAfter,
                                                   int pageSize) {
        logger.info("DeterminationService: ---getResultSearch поиск услуги или товар по имени: " +
                requestSearchByLineDTO.getLineSearch());

        ResponseResultSearchDTO resultSearchDTO = new ResponseResultSearchDTO();

        logger.info("DeterminationService: ---getResultSearch поиск товара по имени: " +
                requestSearchByLineDTO.getLineSearch());

        ResponseProductSlice products = productService.searchByLine(
                requestSearchByLineDTO,
                uuidTradePoint,
                uuidProductAfter,
                pageSize);

        logger.info("DeterminationService: ---getResultSearch поиск услуг по имени: " +
                requestSearchByLineDTO.getLineSearch());

        List<ResponseCatalogServiceDTO> responseCatalogServiceDTOS =
                catalogService.getServicesCatalogDTOByKeyword(requestSearchByLineDTO.getLineSearch());

        logger.info("DeterminationService: ---getResultSearch поиск тарифных планов по имени: " +
                requestSearchByLineDTO.getLineSearch());

        List<ResponseTariffPlanDTO> responseTariffPlanDTOS = tariffPlanService.getTariffPlansDTOByKeyword(
                requestSearchByLineDTO.getLineSearch());

        resultSearchDTO.setProductFoundDTO(products);
        resultSearchDTO.setCatalogServiceDTOS(responseCatalogServiceDTOS);
        resultSearchDTO.setTariffPlanDTOS(responseTariffPlanDTOS);

        return resultSearchDTO;
    }
}
