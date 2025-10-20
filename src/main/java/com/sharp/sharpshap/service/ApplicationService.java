package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductChangeDTO;
import com.sharp.sharpshap.dto.RequestUuidTradePoint;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.dto.ResponseProductDTO;
import com.sharp.sharpshap.entity.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ProductChangeRequestService productChangeRequestService;
    private final ProductService productService;
    private final String STATUS_PRODUCT_AVAILABLE = "AVAILABLE";
    private final String STATUS_PRODUCT_REMOVABLE = "REMOVABLE";
    private final String STATUS_PRODUCT_MOVING = "MOVING";
    private final String STATUS_PRODUCT_PENDING = "PENDING";
    public static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    public List<ResponseProductChangeRequestDTO> getAllApplicationsForAdminProductChangeRequestDTO() {
        logger.info("ApplicationForAdminService: ---getAllApplicationsForAdminProductChangeRequestDTO");
        return productChangeRequestService.getAllProductChangeDTOForAdmin();
    }

    public List<ResponseProductChangeRequestDTO> getAllApplicationsProductChangeRequestFilterForUserDTO(UUID uuidTradePoint) {
        logger.info("ApplicationForAdminService: ---getAllApplicationsProductChangeRequestFilterForUserDTO");
        return productChangeRequestService.getApplicationsFilteredByStatusAndTradePointProductChangeDTO(uuidTradePoint);
    }

    public List<ResponseProductDTO> getAllApplicationsForAdminAddNewProduct() {
        logger.info("ApplicationForAdminService: ---getAllApplicationsAddNewProduct");
        return productService.getNewProductsDTOForApplications();
    }

    public List<ResponseProductDTO> getAllApplicationsAddNewProductFilterForUserDTO(UUID uuidTradePoint) {
        logger.info("ApplicationForAdminService: ---getAllApplicationsAddNewProductFilterForUserDTO");
        return productService.getApplicationsNewProductFilteredByTradePointAndStatusProductDTO(uuidTradePoint);
    }

    public void acceptNewProduct(UUID uuidProduct) {
        logger.info("ApplicationForAdminService: ---acceptNewProduct");
        productService.acceptNewProduct(uuidProduct);
    }

    @Transactional
    public void acceptProductChange(UUID uuidProductChange) {
        logger.info("ApplicationForAdminService: ---acceptProductChange заявка принята на изменения продукта");
        logger.info("ApplicationForAdminService: ---acceptProductChange удаление заявки");
        Product changeableProduct = productChangeRequestService.deleteChangeableProduct(uuidProductChange);
        logger.info("ApplicationForAdminService: ---acceptProductChange изменение статуса продукту и сохракнение");
        productService.setStatusProductByNameAndSave(STATUS_PRODUCT_AVAILABLE, changeableProduct.getId());
    }

    @Transactional
    public void acceptRemovedProduct(UUID uuidProductChange) {
        logger.info("ApplicationForAdminService: ---acceptRemovedProduct заявка принята на удаление продукта");
        logger.info("ApplicationForAdminService: ---acceptRemovedProduct удаляем заявку из базы");
        Product removedProduct = productChangeRequestService.deleteChangeableProduct(uuidProductChange);
        logger.info("ApplicationForAdminService: ---acceptRemovedProduct удаляем продукт");
        productService.deleteProduct(removedProduct.getId());
    }

    @Transactional
    public void cancelRemovedProduct(UUID uuidProductChange) {
        logger.info("ApplicationForAdminService: ---cancelRemovedProduct: удаляем заявку");
        Product availableProduct = productChangeRequestService.deleteChangeableProduct(uuidProductChange);
        logger.info("ApplicationForAdminService: ---cancelRemovedProduct: возобновляем продукт к продажам");
        productService.setStatusProductByNameAndSave(STATUS_PRODUCT_AVAILABLE, availableProduct.getId());
    }

    public void cancelAddNewProduct(UUID uuidProduct) {
        logger.info("ApplicationForAdminService: ---cancelAddNewProduct");
        productService.cancelAddNewProduct(uuidProduct);
    }

    public void cancelChangeableProduct(UUID uuidProductChange) {
        logger.info("ApplicationForAdminService: ---cancelChangeableProduct изменим статус");
        productChangeRequestService.cancelChangeableProduct(uuidProductChange);
    }

    public void deleteNewProduct(UUID uuidProduct) {
        logger.info("ApplicationForAdminService: ---deleteNewProduct");
        productService.deleteProduct(uuidProduct);
    }

    @Transactional
    public void deleteChangeableProduct(UUID uuidProductChange) {
        logger.info("ApplicationForAdminService: ---deleteChangeableProduct");
        Product availableProduct = productChangeRequestService.deleteChangeableProduct(uuidProductChange);
        logger.info("ApplicationForAdminService: ---deleteChangeableProduct: возобновляем продукт к продажам");
        productService.setStatusProductByNameAndSave(STATUS_PRODUCT_AVAILABLE, availableProduct.getId());
    }

    public void updateChangeableProduct(UUID uuidProductChange, UUID uuidUser, ProductChangeDTO productChangeDTO) {
        logger.info("ApplicationForAdminService: ---updateNewAddProduct");
        productChangeRequestService.updateChangeableProduct(uuidProductChange, uuidUser, productChangeDTO);
    }

    @Transactional
    public void receivedTheProduct(UUID uuidProductChange, UUID uuidTradePoint) {
        logger.info("ApplicationForAdminService: ---receivedTheProduct товар прибыл на точку");
        Product movingProduct = productChangeRequestService.deleteChangeableProduct(uuidProductChange);
        productService.setTradePoint(movingProduct, uuidTradePoint);
    }

    @Transactional
    public void deleteProductForUser(UUID uuidProduct, UUID uuidUser) {
        logger.info("ApplicationForAdminService: ---deleteProductForUser изменяем статус продукту на : " +
                STATUS_PRODUCT_PENDING);

        Product product = productService.setStatusProductByNameAndSave(STATUS_PRODUCT_PENDING, uuidProduct);
        productChangeRequestService.productToProductChangeRequest(product, STATUS_PRODUCT_REMOVABLE);
    }

    @Transactional
    public void sendToAnotherTradePoint(UUID uuidProduct, UUID uuidTradePoint) {
        logger.info("ApplicationForAdminService: ---sendToAnotherTradePoint создание заячвки на отправление точки");
        Product product = productService.sendToAnotherTradePoint(
                uuidProduct,
                uuidTradePoint
        );
        productChangeRequestService.productToProductChangeRequest(
                product,
                STATUS_PRODUCT_MOVING,
                uuidTradePoint
        );
    }


}
