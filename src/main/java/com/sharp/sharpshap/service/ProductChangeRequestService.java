package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductChangeDTO;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import com.sharp.sharpshap.exceptions.ProductChangeRequestNotFoundException;
import com.sharp.sharpshap.repository.ProductChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductChangeRequestService {
    private final ProductChangeRequestRepository productChangeRequestRepository;
    private final UtilGenerateSKUService utilGenerateSKUService;
    private final StatusProductService statusProductService;
    private final String STATUS_PRODUCT_PENDING_APPROVAL = "PENDING_APPROVAL";
    public static final Logger logger = LoggerFactory.getLogger(ProductChangeRequestService.class);

    public ProductChangeRequest getByUuid(UUID uuidProductChangeRequest) {
        return productChangeRequestRepository.findById(uuidProductChangeRequest).orElseThrow(() ->
                new ProductChangeRequestNotFoundException("не найдено в БД"));
    }

    private ProductChangeRequest createProductChangeRequest(ProductChangeDTO productChangeDTO,
                                                            Product product,
                                                            User user,
                                                            TradePoint tradePoint,
                                                            EnumCurrency currency,
                                                            CategorySubcategory categorySubcategory) {
        logger.info("ProductChangeRequestService: ---createProductChangeRequest создаём обьект хранящий новые значения для продукта ");
        EnumStatusProduct statusProduct = statusProductService.findByName(STATUS_PRODUCT_PENDING_APPROVAL);

        ProductChangeRequest productChangeRequest = new ProductChangeRequest();
        productChangeRequest.setProduct(product);
        productChangeRequest.setModel(productChangeDTO.getModel());
        productChangeRequest.setBrand(productChangeDTO.getBrand());
        productChangeRequest.setCharacteristics(productChangeDTO.getCharacteristics());
        productChangeRequest.setQuantity(productChangeDTO.getQuantity());
        productChangeRequest.setCurrency(currency);
        productChangeRequest.setCurrencyRate(productChangeDTO.getRateCurrency());
        productChangeRequest.setPriceWithVat(productChangeDTO.getPriceWithVat());
        productChangeRequest.setPriceSelling(productChangeDTO.getPriceSelling());
        productChangeRequest.setStatusProduct(statusProduct);
        productChangeRequest.setCategorySubcategory(categorySubcategory);
        productChangeRequest.setSku(utilGenerateSKUService.generationFieldSku(categorySubcategory,
                productChangeDTO.getBrand(), productChangeDTO.getModel(), productChangeDTO.getCharacteristics()));
        productChangeRequest.setUser(user);
        productChangeRequest.setTradePoint(tradePoint);
        return productChangeRequest;

    }

    public ProductChangeRequest save(ProductChangeDTO productChangeDTO,
                                     Product product,
                                     User user,
                                     TradePoint tradePoint,
                                     EnumCurrency currency,
                                     CategorySubcategory categorySubcategory) {
        logger.info("ProductChangeRequestService: ---createProductChangeRequest создаём и сохраняем в бд обьект хранящий новые значения для продукта ");
        ProductChangeRequest productChangeRequest = createProductChangeRequest(
                productChangeDTO,
                product,
                user,
                tradePoint,
                currency,
                categorySubcategory);
        return productChangeRequestRepository.save(productChangeRequest);
    }

    public List<ResponseProductChangeRequestDTO> getAllProductChangeDTO() {
        logger.info("ProductChangeRequestService: ---getAllProductChangeDTO");
        List<ProductChangeRequest> productChangeRequests = getAllProductChange();
        return productChangeRequests.stream()
                .map(productChangeRequest -> toDTO(productChangeRequest))
                .collect(Collectors.toList());
    }

    private List<ProductChangeRequest> getAllProductChange() {
        logger.info("ProductChangeRequestService: ---getAllProductChange");
        List<ProductChangeRequest> productChangeRequests = getFilterProductChangeRequestByStatus(STATUS_PRODUCT_PENDING_APPROVAL);
        return productChangeRequests;
    }

    private ResponseProductChangeRequestDTO toDTO(ProductChangeRequest productChangeRequest) {
        logger.info("ProductChangeRequestService: ---toDTO");
        return new ResponseProductChangeRequestDTO(
                productChangeRequest.getId(),
                productChangeRequest.getProduct(),
                Optional.ofNullable(productChangeRequest.getBrand()).orElse(" "),
                Optional.ofNullable(productChangeRequest.getModel()).orElse(" "),
                Optional.ofNullable(productChangeRequest.getCharacteristics()).orElse(" "),
                productChangeRequest.getQuantity(),
                productChangeRequest.getCurrency(),
                productChangeRequest.getCurrencyRate(),
                productChangeRequest.getPriceWithVat(),
                productChangeRequest.getPriceSelling(),
                productChangeRequest.getStatusProduct(),
                productChangeRequest.getCategorySubcategory(),
                productChangeRequest.getSku(),
                productChangeRequest.getUser(),
                productChangeRequest.getTradePoint()
        );
    }
    private List<ProductChangeRequest> getFilterProductChangeRequestByStatus(String status){
        EnumStatusProduct statusProduct = statusProductService.findByName(STATUS_PRODUCT_PENDING_APPROVAL);
        return productChangeRequestRepository.findByStatusProduct(statusProduct);
    }
}
