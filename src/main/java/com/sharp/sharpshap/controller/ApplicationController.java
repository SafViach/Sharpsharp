package com.sharp.sharpshap.controller;

import com.sharp.sharpshap.dto.ProductChangeDTO;
import com.sharp.sharpshap.dto.RequestUuidTradePoint;
import com.sharp.sharpshap.dto.ResponseApplicationsDTO;
import com.sharp.sharpshap.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    public static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @GetMapping("/get-applications/admin")
    public ResponseEntity<ResponseApplicationsDTO> getAllApplicationsForAdmin() {
        logger.info("ApplicationController: ---getAllApplicationsForAdmin");
        ResponseApplicationsDTO responseApplicationsDTO = new ResponseApplicationsDTO();

        responseApplicationsDTO.setApplicationsProductChangeRequestDTOS(
                applicationService.getAllApplicationsForAdminProductChangeRequestDTO());
        responseApplicationsDTO.setApplicationsNewProductDTOS(
                applicationService.getAllApplicationsForAdminAddNewProduct());

        return ResponseEntity.ok().body(responseApplicationsDTO);
    }

    @GetMapping("/get-applications/user")
    public ResponseEntity<ResponseApplicationsDTO> getAllApplicationsFilterForUser(
            @RequestAttribute(name = "uuidTradePoint") UUID uuidTradePoint) {
        logger.info("ApplicationController: ---getAllApplicationsFilterForUser");
        ResponseApplicationsDTO responseApplicationsDTO = new ResponseApplicationsDTO();
        responseApplicationsDTO.setApplicationsNewProductDTOS(
                applicationService.getAllApplicationsAddNewProductFilterForUserDTO(uuidTradePoint));
        responseApplicationsDTO.setApplicationsProductChangeRequestDTOS(
                applicationService.getAllApplicationsProductChangeRequestFilterForUserDTO(uuidTradePoint)
        );
        return ResponseEntity.ok().body(responseApplicationsDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/accept/new-add-product/{uuidProduct}")
    public ResponseEntity acceptNewProduct(@PathVariable(name = "uuidProduct") UUID uuidProduct) {
        logger.info("ApplicationController: ---acceptNewProduct заявка на добавление нового товара " +
                "принята администратором");
        applicationService.acceptNewProduct(uuidProduct);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/accept/changeable-product/{uuidProductChange}")
    public ResponseEntity acceptChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---acceptChangeableProduct заявка на изменение продукта " +
                " принята администратором");
        applicationService.acceptProductChange(uuidProductChange);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/accept/removed-product/{uuidProductChange}")
    public ResponseEntity acceptRemovedChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---acceptRemovableChangeableProduct заявка на удаление продукта" +
                " принята администратором");
        applicationService.acceptRemovedProduct(uuidProductChange);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/cancel/removed-product/{uuidProductChange}")
    public ResponseEntity cancelRemovedChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---cancelRemovableChangeableProduct заявка на удаление продукта" +
                " отклонена администратором");
        applicationService.cancelRemovedProduct(uuidProductChange);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/cancel/new-add-product/{uuidProduct}")
    public ResponseEntity cancelNewAddProduct(@PathVariable(name = "uuidProduct") UUID uuidProduct) {
        logger.info("ApplicationController: ---cancelNewAddProduct заявка на добавление нового товара " +
                " отменена администратором");
        applicationService.cancelAddNewProduct(uuidProduct);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/cancel/changeable-product/{uuidProductChange}")
    public ResponseEntity cancelChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---cancelChangeableProduct заявка на изменение товара " +
                " отменена администратором");
        applicationService.cancelChangeableProduct(uuidProductChange);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/new-add-product/{uuidProduct}")
    public ResponseEntity deleteNewAddProduct(@PathVariable(name = "uuidProduct") UUID uuidProduct) {
        logger.info("ApplicationController: ---cancelChangeableProduct заявка на добавление нового  товара " +
                " удалена пользователем");
        applicationService.deleteNewProduct(uuidProduct);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/changeable-product/{uuidProductChange}")
    public ResponseEntity deleteChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---cancelChangeableProduct заявка на изменение товара " +
                " отменена пользователем");
        applicationService.deleteChangeableProduct(uuidProductChange);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update/changeable-product/{uuidProductChange}")
    public ResponseEntity updateChangeableProduct(@PathVariable(name = "uuidProductChange") UUID uuidProductChange,
                                                  @RequestAttribute(name = "uuidUser") UUID uuidUser,
                                                  @Valid @RequestBody ProductChangeDTO productChangeDTO) {
        logger.info("ApplicationController: ---updateChangeableProduct изменение нового продукта");
        applicationService.updateChangeableProduct(uuidProductChange, uuidUser, productChangeDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("received-the-product/{uuidProductCange}")
    public ResponseEntity receivedTheProduct(@CookieValue(name = "uuidTradePoint") UUID uuidTradePoint,
                                             @PathVariable(name = "uuidProductCange") UUID uuidProductChange) {
        logger.info("ApplicationController: ---receivedTheProduct товар отправленный на точку был принят");
        applicationService.receivedTheProduct(uuidProductChange, uuidTradePoint);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("delete-the-product-for-user/{uuidProduct}")
    public ResponseEntity deleteProductForUser(@PathVariable(name = "uuidProduct") UUID uuidProduct,
                                               @RequestAttribute(name = "uuidUser") UUID uuidUser) {
        logger.info("ApplicationController: ---deleteProductForUser создание заявки на удаление продукта пользователем");
        applicationService.deleteProductForUser(uuidProduct, uuidUser);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{uuidProduct}")
    public ResponseEntity sendToAnotherTradePoint(@PathVariable(name = "uuidProduct") UUID uuidProduct,
                                                  @RequestBody RequestUuidTradePoint requestUuidTradePoint){
        logger.info("ApplicationController: ---sendToAnotherTradePoint отправка продукта на другую точку");
        applicationService.sendToAnotherTradePoint(uuidProduct,requestUuidTradePoint.getUuidTradePoint());
        return ResponseEntity.ok().build();
    }


}
