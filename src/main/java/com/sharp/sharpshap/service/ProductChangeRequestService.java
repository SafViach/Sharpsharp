package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductChangeDTO;
import com.sharp.sharpshap.dto.ResponseProductChangeRequestDTO;
import com.sharp.sharpshap.dto.ResponseProductDTO;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import com.sharp.sharpshap.exceptions.*;
import com.sharp.sharpshap.repository.CategoryRepository;
import com.sharp.sharpshap.repository.ProductChangeRequestRepository;
import com.sharp.sharpshap.repository.ProductRepository;
import com.sharp.sharpshap.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductChangeRequestService {
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategorySubcategoryService categorySubcategoryService;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final ProductChangeRequestRepository productChangeRequestRepository;
    private final ProductRepository productRepository;
    private final UtilGenerateSKUService utilGenerateSKUService;
    private final StatusProductService statusProductService;
    private final TradePointService tradePointService;
    private final String STATUS_PRODUCT_MOVING = "MOVING";
    private final String STATUS_PRODUCT_REMOVABLE = "REMOVABLE";
    private final String STATUS_PRODUCT_AVAILABLE = "AVAILABLE";
    private final String STATUS_PRODUCT_CANCEL = "CANCEL";
    private final String STATUS_PRODUCT_EXAMINATION = "EXAMINATION";
    public static final Logger logger = LoggerFactory.getLogger(ProductChangeRequestService.class);

    public ProductChangeRequest getByUuid(UUID uuidProductChangeRequest) {
        return productChangeRequestRepository.findById(uuidProductChangeRequest).orElseThrow(() ->
                new ProductChangeRequestNotFoundException("не найдено в БД"));
    }

    private ProductChangeRequest setStatus(ProductChangeRequest productChangeRequest,
                                           String statusProduct) {
        EnumStatusProduct enumStatusProduct = statusProductService.findByName(statusProduct);
        logger.info("ProductChangeRequestService: ---setStatus изменяем статус продукту: " + productChangeRequest.getSku() +
                " со статусом " + productChangeRequest.getStatusProduct() + " на статус: " + statusProduct);
        productChangeRequest.setStatusProduct(enumStatusProduct);
        return productChangeRequest;
    }

    private ProductChangeRequest addProductChangeRequest(ProductChangeDTO productChangeDTO,
                                                         Product product,
                                                         User user,
                                                         TradePoint tradePoint,
                                                         EnumCurrency currency,
                                                         CategorySubcategory categorySubcategory) {
        logger.info("ProductChangeRequestService: ---createProductChangeRequest создаём обьект хранящий " +
                "новые значения для продукта ");
        EnumStatusProduct statusProduct = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);

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
        ProductChangeRequest productChangeRequest = addProductChangeRequest(
                productChangeDTO,
                product,
                user,
                tradePoint,
                currency,
                categorySubcategory);
        return productChangeRequestRepository.save(productChangeRequest);
    }

    public void save(ProductChangeRequest productChangeRequest) {
        productChangeRequestRepository.save(productChangeRequest);
    }

    public List<ResponseProductChangeRequestDTO> getAllProductChangeDTOForAdmin() {
        logger.info("ProductChangeRequestService: ---getAllProductChangeDTOForAdmin");
        List<ProductChangeRequest> productChangeRequests = getAllProductChange();
        return productChangeRequests.stream()
                .map(productChangeRequest -> toDTO(productChangeRequest))
                .collect(Collectors.toList());
    }

    public List<ResponseProductChangeRequestDTO> getApplicationsFilteredByStatusAndTradePointProductChangeDTO(
            UUID uuidTradePoint) {
        logger.info("ProductChangeRequestService: ---getAllProductChangeDTO");
        List<ProductChangeRequest> productChangeRequests = getFilterByStatusAndTradePointAllProductChange(uuidTradePoint);
        return productChangeRequests.stream()
                .map(productChangeRequest -> toDTO(productChangeRequest))
                .collect(Collectors.toList());
    }

    private List<ProductChangeRequest> getAllProductChange() {
        logger.info("ProductChangeRequestService: ---getAllProductChange");
        EnumStatusProduct statusProductExamination = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);
        EnumStatusProduct statusProductMoving = statusProductService.findByName(STATUS_PRODUCT_MOVING);
        EnumStatusProduct statusProductRemovable = statusProductService.findByName(STATUS_PRODUCT_REMOVABLE);

        List<EnumStatusProduct> statusProduct = List.of(
                statusProductExamination,
                statusProductMoving,
                statusProductRemovable);
        return productChangeRequestRepository.findByStatusProductIn(statusProduct);
    }

    private List<ProductChangeRequest> getFilterByStatusAndTradePointAllProductChange(UUID uuidTradePoint) {
        logger.info("ProductChangeRequestService: ---getFilterByStatusAndTradePointAllProductChange");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        EnumStatusProduct statusProductCancel = statusProductService.findByName(STATUS_PRODUCT_CANCEL);
        EnumStatusProduct statusProductExamination = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);
        EnumStatusProduct statusProductMoving = statusProductService.findByName(STATUS_PRODUCT_MOVING);
        EnumStatusProduct statusProductRemovable = statusProductService.findByName(STATUS_PRODUCT_REMOVABLE);

        List<EnumStatusProduct> statusProduct = List.of(
                statusProductCancel,
                statusProductExamination,
                statusProductMoving,
                statusProductRemovable);
        logger.info("ProductService:  getApplicationsNewProductFilteredByTradePointAndStatusProductDTO получение " +
                "товаров статуса: " + STATUS_PRODUCT_CANCEL + "- отмененный администратором" +
                " и статуса: " + STATUS_PRODUCT_EXAMINATION + "-в наличие и на проверке" +
                " соответствующей точке: " + tradePoint.getName());
        List<ProductChangeRequest> productChangeRequests = getFilterProductChangeRequestByStatusAndTradePoint(
                statusProduct, tradePoint);
        return productChangeRequests;
    }

    private ResponseProductChangeRequestDTO toDTO(ProductChangeRequest productChangeRequest) {
        logger.info("ProductChangeRequestService: ---toDTO");
        return new ResponseProductChangeRequestDTO(
                productChangeRequest.getId(),
                ResponseProductDTO.toInResponseProductDTO(productChangeRequest.getProduct()),
                Optional.ofNullable(productChangeRequest.getBrand()).orElse(" "),
                Optional.ofNullable(productChangeRequest.getModel()).orElse(" "),
                Optional.ofNullable(productChangeRequest.getCharacteristics()).orElse(" "),
                productChangeRequest.getQuantity(),
                productChangeRequest.getCurrency().getDescription(),
                productChangeRequest.getCurrencyRate(),
                productChangeRequest.getPriceWithVat(),
                productChangeRequest.getPriceSelling(),
                productChangeRequest.getStatusProduct().getStatus(),
                productChangeRequest.getSku(),
                productChangeRequest.getUser().getFirstName() + " " + productChangeRequest.getUser().getLastName(),
                productChangeRequest.getTradePoint().getName()
        );
    }



    private List<ProductChangeRequest> getFilterProductChangeRequestByStatusAndTradePoint(List<EnumStatusProduct>
                                                                                                  statusProduct,
                                                                                          TradePoint tradePoint) {
        logger.info("ProductService:  getApplicationsNewProductFilteredByTradePointAndStatusProductDTO получение " +
                "товаров статуса: " + STATUS_PRODUCT_CANCEL + "- отмененный администратором" +
                " соответствующей точке: " + " и статуса: " + STATUS_PRODUCT_EXAMINATION +
                tradePoint.getName());
        return productChangeRequestRepository.findByStatusProductInAndTradePoint(statusProduct, tradePoint);
    }

    private void deleteProductChangeRequest(ProductChangeRequest productChangeRequest) {
        logger.info("ProductChangeRequestService: ---deleteProductChangeRequest удаление заявки");
        productChangeRequestRepository.delete(productChangeRequest);
    }

    private Product getProductByUuid(UUID uuidProduct) {
        return productRepository.findById(uuidProduct).orElseThrow(() ->
                new ProductNotFoundException("Продукт по uuid не найден"));
    }

    @Transactional
    public void acceptRemovedProduct(UUID uuidProductRequest) {
        ProductChangeRequest productChangeRequest = getByUuid(uuidProductRequest);
        Product product = productChangeRequest.getProduct();
        logger.info("ProductChangeRequestService: ---acceptRemovableProduct принимаем заявку удаление продукта: " +
                product.getSku());
        productChangeRequestRepository.delete(productChangeRequest);
        productRepository.delete(product);
    }


    private Product updateProduct(ProductChangeRequest productChangeRequest,
                                  Product oldProduct,
                                  EnumStatusProduct statusProduct) {
        logger.info("ProductChangeRequestService: ---updateProduct изменение продукта");
        oldProduct.setBrand(Optional.ofNullable(
                productChangeRequest.getBrand()).orElse(" "));
        oldProduct.setModel(Optional.ofNullable(
                productChangeRequest.getModel()).orElse(" "));
        oldProduct.setCharacteristics(Optional.ofNullable(
                productChangeRequest.getCharacteristics()).orElse(" "));
        oldProduct.setQuantity(oldProduct.getQuantity());
        oldProduct.setCurrency(productChangeRequest.getCurrency());
        oldProduct.setCurrencyRate(productChangeRequest.getCurrencyRate());
        oldProduct.setPriceWithVat(productChangeRequest.getPriceWithVat());
        oldProduct.setPriceSelling(productChangeRequest.getPriceSelling());
        oldProduct.setStatusProduct(statusProduct);
        oldProduct.setCategorySubcategory(productChangeRequest.getCategorySubcategory());
        oldProduct.setSku(productChangeRequest.getSku());
        oldProduct.setUserAcceptedProduct(productChangeRequest.getUser());
        oldProduct.setTradePoint(productChangeRequest.getTradePoint());

        return oldProduct;
    }

    //написать метод который отменяет изменения продукта пользователем
    //доступен для ADMIN
    //изменения - заявке на изменения продукта присваивается статус CANCEL
    //          - продукту статус остаётся PENDING
    @Transactional
    public void cancelChangeableProduct(UUID uuidProductRequest) {
        ProductChangeRequest productChangeRequest = getByUuid(uuidProductRequest);
        logger.info("ProductChangeRequestService: ---cancelChangeableProduct отмена заявки на изменения продукта");
        productChangeRequest = setStatus(productChangeRequest, STATUS_PRODUCT_CANCEL);
        productChangeRequestRepository.save(productChangeRequest);
    }

    //написать метод который удаляет заявку для изменения продукта
    //доступна всем
    @Transactional
    public Product deleteChangeableProduct(UUID uuidProductRequest) {
        ProductChangeRequest productChangeRequest = getByUuid(uuidProductRequest);
        Product product = productChangeRequest.getProduct();
        logger.info("ProductChangeRequestService: ---deleteChangeableProduct удаление заявки : " +
                product.getSku() + " на " + productChangeRequest.getSku());
        checkStatusProduct(productChangeRequest);
        deleteProductChangeRequest(productChangeRequest);
        return product;
    }

    //написать метод который изменяет изменения к продукту
    public void updateChangeableProduct(UUID uuidProductChange, UUID uuidUser, ProductChangeDTO productChangeDTO) {
        ProductChangeRequest oldChangeRequest = getByUuid(uuidProductChange);
        User user = userService.getUserById(uuidUser);
        logger.info("ProductChangeRequestService: ---updateChangeableProduct изменения заявки на изменение продукта. " +
                "изменяемый продукт : " + oldChangeRequest.getProduct().getSku() +
                "заявка на изменения :" + oldChangeRequest.getSku());
        checkStatusProduct(oldChangeRequest);
        oldChangeRequest.setBrand(Optional.ofNullable(productChangeDTO.getBrand()).orElse(" "));
        oldChangeRequest.setModel(Optional.ofNullable(productChangeDTO.getModel()).orElse(" "));
        oldChangeRequest.setCharacteristics(Optional.ofNullable(productChangeDTO.getCharacteristics())
                .orElse(" "));
        Category category = categoryRepository.findById(productChangeDTO.getUuidCategory()).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));

        Subcategory subcategory = subcategoryRepository.findById(productChangeDTO.getUuidSubcategory()).orElseThrow(() ->
                new SubcategoryNotFoundException("SubcategoryService: ---getSubcategoryByUuid " +
                        "подкатегория по UUID не найдена в БД"));

        CategorySubcategory categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(
                category, subcategory);
        oldChangeRequest.setCategorySubcategory(categorySubcategory);
        oldChangeRequest.setQuantity(productChangeDTO.getQuantity());
        oldChangeRequest.setPriceWithVat(productChangeDTO.getPriceWithVat());
        oldChangeRequest.setCurrencyRate(productChangeDTO.getRateCurrency());
        EnumCurrency currency = currencyService.getById(productChangeDTO.getUuidCurrency());
        oldChangeRequest.setCurrency(currency);
        oldChangeRequest.setPriceSelling(productChangeDTO.getPriceSelling());
        oldChangeRequest.setUser(user);
        productChangeRequestRepository.save(oldChangeRequest);
    }

    private void checkStatusProduct(ProductChangeRequest changeRequest) {
        if (!changeRequest.getStatusProduct().getStatus().equals(STATUS_PRODUCT_CANCEL) ||
                !changeRequest.getStatusProduct().equals(STATUS_PRODUCT_EXAMINATION) ||
                !changeRequest.getStatusProduct().equals(STATUS_PRODUCT_REMOVABLE)) {
            throw new ProductChangeRequestException("Заявка не равна статусу: " + STATUS_PRODUCT_CANCEL + " , " +
                    STATUS_PRODUCT_EXAMINATION + " , " + STATUS_PRODUCT_REMOVABLE);
        }

    }
    public ProductChangeRequest productToProductChangeRequest(Product product,String statusProduct) {
        EnumStatusProduct status = statusProductService.findByName(statusProduct);
        ProductChangeRequest productChangeRequest = new ProductChangeRequest();

        productChangeRequest.setProduct(product);
        productChangeRequest.setBrand(Optional.ofNullable(product.getBrand()).orElse(" "));
        productChangeRequest.setModel(Optional.ofNullable(product.getModel()).orElse(" "));
        productChangeRequest.setCharacteristics(Optional.ofNullable(product.getCharacteristics())
                .orElse(" "));
        productChangeRequest.setQuantity(product.getQuantity());
        productChangeRequest.setCurrency(product.getCurrency());
        productChangeRequest.setCurrencyRate(product.getCurrencyRate());
        productChangeRequest.setPriceWithVat(product.getPriceWithVat());
        productChangeRequest.setPriceSelling(product.getPriceSelling());

        productChangeRequest.setStatusProduct(status);

        productChangeRequest.setCategorySubcategory(product.getCategorySubcategory());
        productChangeRequest.setSku(product.getSku());
        productChangeRequest.setUser(product.getUserAcceptedProduct());
        productChangeRequest.setTradePoint(product.getTradePoint());

        return productChangeRequestRepository.save(productChangeRequest);
    }

    public ProductChangeRequest productToProductChangeRequest(Product product,String statusProduct, UUID uuidTradePoint) {
        EnumStatusProduct status = statusProductService.findByName(statusProduct);
        ProductChangeRequest productChangeRequest = new ProductChangeRequest();
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);

        productChangeRequest.setProduct(product);
        productChangeRequest.setBrand(Optional.ofNullable(product.getBrand()).orElse(" "));
        productChangeRequest.setModel(Optional.ofNullable(product.getModel()).orElse(" "));
        productChangeRequest.setCharacteristics(Optional.ofNullable(product.getCharacteristics())
                .orElse(" "));
        productChangeRequest.setQuantity(product.getQuantity());
        productChangeRequest.setCurrency(product.getCurrency());
        productChangeRequest.setCurrencyRate(product.getCurrencyRate());
        productChangeRequest.setPriceWithVat(product.getPriceWithVat());
        productChangeRequest.setPriceSelling(product.getPriceSelling());

        productChangeRequest.setStatusProduct(status);

        productChangeRequest.setCategorySubcategory(product.getCategorySubcategory());
        productChangeRequest.setSku(product.getSku());
        productChangeRequest.setUser(product.getUserAcceptedProduct());
        productChangeRequest.setTradePoint(tradePoint);

        return productChangeRequestRepository.save(productChangeRequest);
    }
}
