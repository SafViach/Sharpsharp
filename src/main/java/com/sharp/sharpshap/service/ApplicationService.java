package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductChangeDTO;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.dto.ResponseProductDTO;
import com.sharp.sharpshap.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ProductChangeRequestService productChangeRequestService;
    private final ProductService productService;
    public static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    public List<ResponseProductChangeRequestDTO> getAllApplicationsForAdminProductChangeRequestDTO(){
        logger.info("ApplicationForAdminService: ---getAllApplicationsForAdminProductChangeRequestDTO");
        return productChangeRequestService.getAllProductChangeDTOForAdmin();
    }
    public List<ResponseProductChangeRequestDTO> getAllApplicationsProductChangeRequestFilterForUserDTO(UUID uuidTradePoint){
        logger.info("ApplicationForAdminService: ---getAllApplicationsProductChangeRequestFilterForUserDTO");
        return productChangeRequestService.getApplicationsFilteredByStatusAndTradePointProductChangeDTO(uuidTradePoint);
    }
    public List<ResponseProductDTO> getAllApplicationsForAdminAddNewProduct(){
        logger.info("ApplicationForAdminService: ---getAllApplicationsAddNewProduct");
        return productService.getNewProductsDTOForApplications();
    }
    public List<ResponseProductDTO> getAllApplicationsAddNewProductFilterForUserDTO(UUID uuidTradePoint){
        logger.info("ApplicationForAdminService: ---getAllApplicationsAddNewProductFilterForUserDTO");
        return productService.getApplicationsNewProductFilteredByTradePointAndStatusProductDTO(uuidTradePoint);
    }
    public void acceptNewProduct(UUID uuidProduct){
        logger.info("ApplicationForAdminService: ---acceptNewProduct");
        productService.acceptNewProduct(uuidProduct);
    }
    public void acceptProductChange(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---acceptProductChange");
        productChangeRequestService.acceptChangeProductRequest(uuidProductChange);
    }
    public void acceptRemovableProduct(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---acceptRemovableProduct");
        productChangeRequestService.acceptRemovableProduct(uuidProductChange);
    }

    public void cancelRemovableProduct(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---cancelRemovableProduct: возобновляем продукт к продажам");
        productService.cancelRemovableProduct(productChangeRequestService.getByUuid(uuidProductChange).getProduct());
        logger.info("ApplicationForAdminService: ---cancelRemovableProduct: удаляем заявку");
        productChangeRequestService.deleteChangeableProduct(uuidProductChange);
    }

    public void cancelAddNewProduct(UUID uuidProduct){
        logger.info("ApplicationForAdminService: ---cancelAddNewProduct");
        productService.cancelAddNewProduct(uuidProduct);
    }
    public void cancelChangeableProduct(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---cancelChangeableProduct");
        productChangeRequestService.cancelChangeableProduct(uuidProductChange);
    }
    public void deleteNewProduct(UUID uuidProduct){
        logger.info("ApplicationForAdminService: ---deleteNewProduct");
        productService.deleteApplicationProduct(uuidProduct);
    }
    public void deleteChangeableProduct(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---deleteChangeableProduct");
        productChangeRequestService.deleteChangeableProduct(uuidProductChange);
    }
    public void updateChangeableProduct(UUID uuidProductChange, UUID uuidUser, ProductChangeDTO productChangeDTO){
        logger.info("ApplicationForAdminService: ---updateNewAddProduct");
        productChangeRequestService.updateChangeableProduct(uuidProductChange , uuidUser, productChangeDTO);
    }
    public void receivedTheProduct(UUID uuidProductChange){
        logger.info("ApplicationForAdminService: ---receivedTheProduct");
        productChangeRequestService.receivedTheProduct(uuidProductChange);
    }
    public void deleteProductForUser(UUID uuidProduct, UUID uuidUser){
        logger.info("ApplicationForAdminService: ---deleteProductForUser");
        productService.deleteProductForUser(uuidProduct, uuidUser);
    }


}
