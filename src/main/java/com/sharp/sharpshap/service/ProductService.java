package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;

import java.time.LocalDateTime;

import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.exceptions.ProductNotFoundException;
import com.sharp.sharpshap.exceptions.SubcategoryNotFoundException;
import com.sharp.sharpshap.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final UtilGenerateSKUService utilGenerateSKUService;
    private final CategorySubcategoryService categorySubcategoryService;
    private final DiscountService discountService;
    private final StatusProductService statusProductService;
    private final UserService userService;
    private final SubcategoryRepository subcategoryRepository; //  : (
    private final CategoryRepository categoryRepository; //  : (
    private final ProductRepository productRepository;
    private final TradePointService tradePointService;
    private final CurrencyService currencyService;
    private final ProductChangeRequestService productChangeRequestService;
    private final String NAME_STATUS_PENDING_APPROVAL = "PENDING_APPROVAL";
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final String STATUS_PRODUCT_AVAILABLE = "AVAILABLE";
    private final String STATUS_PRODUCT_PENDING_APPROVAL = "PENDING_APPROVAL";

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createdProduct(ProductCreateDTO productCreateDTO, UUID uuidCategory,
                               UUID uuidSubcategory, UUID uuidUser, UUID uuidTradePoint) {
        logger.info("ProductService: ---createdProduct добавление продукта");
        Product product = new Product();

        logger.info("ProductService: ---createdProduct discountService.getByAmountDiscount(BigDecimal.ZERO);");
        Discount discount = discountService.getByAmountDiscount(BigDecimal.ZERO);

        logger.info("ProductService: ---createdProduct categoryService.getCategoryById(categoryUUID);");
        Category category = categoryRepository.findById(uuidCategory).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));

        Subcategory subcategory = null;

        if (uuidSubcategory != null) {
            subcategory = subcategoryRepository.findById(uuidSubcategory).orElseThrow(() ->
                    new SubcategoryNotFoundException("SubcategoryService: ---getSubcategoryByUuid " +
                            "подкатегория по UUID не найдена в БД"));
            logger.info("ProductService: ---createdProduct subcategory = " + subcategory.getName());
        } else {
            logger.info("ProductService: ---createdProduct subcategory == null ");
        }

        User user = userService.getUserById(uuidUser);
        logger.info("ProductService: ---createdProduct продукт добавляет" + user.getFirstName() + " " + user.getLastName());

        EnumCurrency currency = currencyService.getById(productCreateDTO.getCurrencyId());
        logger.info("ProductService: ---createdProduct продукт принят по курсу : " + currency.getDescription() + " = " + currency.getRate());

        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService: ---createdProduct продукт добавляется на точку :" + tradePoint.getName());

        EnumStatusProduct statusProduct = statusProductService.findByName(NAME_STATUS_PENDING_APPROVAL);
        logger.info("ProductService: ---createdProduct присваеваем статус продукту : " + statusProduct.getStatus());

        CategorySubcategory categorySubcategory = new CategorySubcategory();

        product.setBrand(productCreateDTO.getBrand());
        logger.info("ProductService: ---createdProduct присваиваю brand :" + productCreateDTO.getBrand());

        product.setModel(productCreateDTO.getModel());
        logger.info("ProductService: ---createdProduct присваиваю model :" + productCreateDTO.getModel());

        product.setCharacteristics(productCreateDTO.getCharacteristics());
        logger.info("ProductService: ---createdProduct присваиваю characteristics :" + productCreateDTO.getCharacteristics());

        product.setQuantity(productCreateDTO.getQuantity());
        logger.info("ProductService: ---createdProduct присваиваю колличество ед товара :" + productCreateDTO.getQuantity());

        product.setCurrency(currency);
        logger.info("ProductService: ---createdProduct присваиваю курс товару :" + currency.getDescription() + " = "
                + currency.getRate());
        product.setCurrencyRate(productCreateDTO.getCurrencyRate());

        product.setPriceWithVat(productCreateDTO.getPriceWithVat());
        logger.info("ProductService: ---createdProduct присваиваю PriceWithVat :" + productCreateDTO.getPriceWithVat());

        product.setPriceSelling(productCreateDTO.getPriceSelling());
        logger.info("ProductService: ---createdProduct присваиваю PriceSelling :" + productCreateDTO.getPriceSelling());

        product.setStatusProduct(statusProduct);
        logger.info("ProductService: ---createdProduct присваиваю статус продукту :" + statusProduct.getStatus());

        product.setDateOfArrival(LocalDateTime.now());
        logger.info("ProductService: ---createdProduct присваиваю дату добавления товара + " + LocalDateTime.now());

        product.setUserAcceptedProduct(user);
        logger.info("ProductService: ---createdProduct присваиваю получателя товара : " + user.getFirstName() + " "
                + user.getLastName());

        product.setTradePoint(tradePoint);
        logger.info("ProductService: ---createdProduct присваиваю товару точку : " + tradePoint.getName());

        product.setDiscount(discount);
        logger.info("ProductService: ---createdProduct присваиваю скидку товару : " + discount.getDiscountAmount());


        categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category, subcategory);

        if (categorySubcategory != null) {
            String nameSubcategory = categorySubcategory.getSubcategory() != null ? categorySubcategory.getSubcategory().getName() :
                    "null";
            logger.info("ProductService: ---createdProduct получили связь : " + categorySubcategory.getCategory().getName() + " - " + nameSubcategory);
            product.setCategorySubcategory(categorySubcategory);
        }

        product.setSku(utilGenerateSKUService.generationFieldSku(
                categorySubcategory,
                productCreateDTO.getBrand(),
                productCreateDTO.getModel(),
                productCreateDTO.getCharacteristics()));

        product.setPriceSelling(calcTheSaleProduct(productCreateDTO, categorySubcategory));
        productRepository.save(product);
    }

    private BigDecimal calcTheSaleProduct(ProductCreateDTO productCreateDTO, CategorySubcategory categorySubcategory) {
        logger.info("ProductService: ---calcTheSaleProduct высчитываем цену товара");
        BigDecimal rateCurrency = productCreateDTO.getCurrencyRate();
        BigDecimal priceWithVat = productCreateDTO.getPriceWithVat();
        logger.info("ProductService: ---calcTheSaleProduct курс по которому будет высчитываться товар равен : " + rateCurrency);
        logger.info("ProductService: ---calcTheSaleProduct цена с НДС : " + priceWithVat);

        BigDecimal marginPercentage = Optional.ofNullable(categorySubcategory.getSubcategory())
                .map(subcategory -> subcategory.getMarginPercentage())
                .orElse(categorySubcategory.getCategory().getMarginPercentage());
        logger.info("ProductService: ---calcTheSaleProduct процент маржи : " + marginPercentage);
        BigDecimal result = priceWithVat.multiply(rateCurrency).multiply(marginPercentage);
        logger.info("ProductService: ---calcTheSaleProduct : " + priceWithVat + " * " + rateCurrency + " * " + marginPercentage + " = " + result);

        return toDot90(result);
    }

    private BigDecimal toDot90(BigDecimal price) {
        // Округляем до целого вниз (берём целую часть)
        BigDecimal wholePart = price.setScale(0, BigDecimal.ROUND_HALF_UP);//RoundingMode.FLOOR);
        // Добавляем 0.90
        return wholePart.add(new BigDecimal("0.90"));
    }

    public boolean checkCategoryForProducts(Category category) {
        return productRepository.existsByCategorySubcategory_Category_Id(category.getId());
    }

    public boolean checkSubcategoryForProducts(Subcategory subcategory) {
        return productRepository.existsByCategorySubcategory_Subcategory_Id(subcategory.getId());
    }

    @Transactional
    public List<ResponseProductDTO> getProductsByUuidTradePoints(UUID uuidTradePoint) {
        logger.info("ProductService: --- getProductsByUuidTradePoints");
        logger.info("ProductService: --- getProductsByUuidTradePoints получаем торговую точку");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService: --- getProductsByUuidTradePoints получаем статус");
        EnumStatusProduct status = statusProductService.findByName("AVAILABLE");
        logger.info("ProductService: --- getProductsByUuidTradePoints фильтруем список продутов по тороговой точке и по статусу");
        List<Product> productsDTOList = productRepository.findByTradePointAndStatusProduct(tradePoint, status);
        logger.info("ProductService: --- getProductsByUuidTradePoints преобразуем список");
        List<ResponseProductDTO> responseProductDTOS = productsDTOList
                .stream()
                .map(product -> toInResponseProductDTO(product))
                .collect(Collectors.toList());
        return responseProductDTOS;
    }

    public ResponseProductDTO toInResponseProductDTO(Product product) {
        logger.info("ProductService: ---transInResponseProductDTO :  преобразуем в  DTO");
        return new ResponseProductDTO(
                product.getId(),
                Optional.ofNullable(product.getBrand()).orElse(""),
                Optional.ofNullable(product.getModel()).orElse(""),
                Optional.ofNullable(product.getCharacteristics()).orElse(""),
                product.getQuantity(),
                Optional.ofNullable(product.getCurrency())
                        .map(EnumCurrency::getDescription)
                        .orElse(""),
                product.getCurrencyRate(),
                Optional.ofNullable(product.getStatusProduct())
                        .map(EnumStatusProduct::getStatus)
                        .orElse(""),
                Optional.ofNullable(product.getCategorySubcategory())
                        .map(CategorySubcategory::getCategory)
                        .map(Category::getName)
                        .orElse(""),
                Optional.ofNullable(product.getCategorySubcategory())
                        .map(CategorySubcategory::getSubcategory)
                        .map(Subcategory::getName)
                        .orElse(""),
                product.getPriceWithVat(),
                product.getPriceSelling(),
                Optional.ofNullable(product.getUserAcceptedProduct())
                        .map(user ->
                                Optional.ofNullable(user.getFirstName()).orElse("") + " " +
                                        Optional.ofNullable(user.getLastName()).orElse("")
                        )
                        .orElse(""),
                product.getSku());
    }

    private Product getProductByUuid(UUID uuidProduct) {
        return productRepository.findById(uuidProduct).orElseThrow(() -> new ProductNotFoundException("Продукт по uuid не найден"));
    }

    public ResponseProductDTO getResponseProductDTOByUuid(UUID uuidProduct) {
        return toInResponseProductDTO(getProductByUuid(uuidProduct));
    }


    public List<ResponseOfTheProductFoundDTO> getProductsByUuidTradePointNoAssignmentTradePointForUser(UUID uuidTradePoint) {
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService: getProductsByUuidTradePointNoAssignmentTradePointForUser получение продуктов на точке: " + tradePoint.getName() +
                "без присвоения пользователю данной точки");
        List<Product> productList = productRepository.findAll();
        return toResponseOfTheProductFoundDTO(filterProductsByStatusAndUuidTradePoint(productList, uuidTradePoint));
    }

    @Transactional
    public List<ResponseOfTheProductFoundDTO> searchByLine(RequestProductSearchDTO productSearchDTO, UUID uuidTradePoint) {
        tradePointService.getByIdTradePoint(uuidTradePoint);
        String line = productSearchDTO.getLineSearch();
        List<ResponseOfTheProductFoundDTO> resultRequest;

        String cleaner = line.trim().toLowerCase();
        String[] parts = cleaner.split("\\s+");
        logger.info("ProductService: searchByLine Начинаем поиск продукта по подстрокам из " + parts.length + " ед.");
        if (parts.length == 1) {
            logger.info("ProductService: searchByLine parts.length == 1 " + parts[0]);
            resultRequest = toResponseOfTheProductFoundDTO(
                    filterProductsByStatusAndUuidTradePoint(productRepository.searchBySkuKeyword(parts[0]), uuidTradePoint));
            return resultRequest;
        } else if (parts.length == 2) {
            logger.info("ProductService: searchByLine parts.length == 2 " + parts[0] + " " + parts[1]);
            return toResponseOfTheProductFoundDTO(
                    filterProductsByStatusAndUuidTradePoint(productRepository.searchByTwoParts(parts[0], parts[1]), uuidTradePoint));
        } else if (parts.length == 3) {
            logger.info("ProductService: searchByLine parts.length == 3 " + parts[0] + " " + parts[1] + " " + parts[2]);
            return toResponseOfTheProductFoundDTO(
                    filterProductsByStatusAndUuidTradePoint(productRepository.searchByThreeParts(parts[0], parts[1], parts[2]), uuidTradePoint));
        } else if (parts.length == 4) {
            logger.info("ProductService: searchByLine parts.length == 4 " + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3]);
            return toResponseOfTheProductFoundDTO(
                    filterProductsByStatusAndUuidTradePoint(productRepository.searchByFourParts(parts[0], parts[1], parts[2], parts[3]), uuidTradePoint));
        } else if (parts.length >= 5) {
            logger.info("ProductService: searchByLine parts.length == 5 " + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
            return toResponseOfTheProductFoundDTO(
                    filterProductsByStatusAndUuidTradePoint(productRepository.searchByFiveParts(parts[0], parts[1], parts[2], parts[3], parts[4]),
                            uuidTradePoint));
        }
        resultRequest = toResponseOfTheProductFoundDTO(
                filterProductsByStatusAndUuidTradePoint(productRepository.searchBySkuKeyword(productSearchDTO.getLineSearch()), uuidTradePoint));
        return resultRequest;
    }

    @Transactional
    public List<Product> filterProductsByStatusAndUuidTradePoint(UUID uuidTradePoint) {
        EnumStatusProduct status = statusProductService.findByName("AVAILABLE");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService:  filterProductsByStatusAndUuidProduct фильтруем список по статусу: " + status +
                " и по торговой точке: ");
        return productRepository.findByTradePointAndStatusProduct(tradePoint, status);
    }

    private List<Product> filterProductsByStatusAndUuidTradePoint(List<Product> products, UUID uuidTradePoint) {
        EnumStatusProduct status = statusProductService.findByName("AVAILABLE");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService:  filterProductsByStatusAndUuidProduct фильтруем список по статусу: " + status +
                " и по торговой точке: ");
        return products.stream()
                .filter(product -> product.getStatusProduct().getStatus().equals(status.getStatus()) &&
                        product.getTradePoint().getId().equals(tradePoint.getId()))
                .collect(Collectors.toList());
    }

    private List<ResponseOfTheProductFoundDTO> toResponseOfTheProductFoundDTO(List<Product> products) {
        logger.info("ProductService:  toResponseOfTheProductFoundDTO преобразуем в DTO: ");
        return products.stream().map(product -> new ResponseOfTheProductFoundDTO(
                product.getId(),
                Optional.ofNullable(product.getBrand()).orElse(""),
                Optional.ofNullable(product.getModel()).orElse(""),
                Optional.ofNullable(product.getCharacteristics()).orElse(""),
                product.getQuantity()
        )).collect(Collectors.toList());
    }


    @Transactional
    public void productChange(UUID uuidProduct, UUID uuidUser, UUID uuidTradePoint, ProductChangeDTO productChangeDTO) {
        logger.info("ProductService:  productChange");
        Product product = getProductByUuid(uuidProduct);
        logger.info("ProductService:  productChange получили прорукт изменяемый: " + product.getSku());
        logger.info("ProductService:  productChange получили прорукт изменяемый product.getVersion(): "
                + product.getVersion());
        logger.info("ProductService:  productChange получили прорукт изменяемый product.getStatusProduct(): "
                + product.getStatusProduct());
        logger.info("ProductService:  productChange получили прорукт изменяемый product.getId(): "
                + product.getId());
        logger.info("ProductService:  productChange получили прорукт изменяемый product.getCurrency().getDescription(): "
                + product.getCurrency().getDescription());


        User user = userService.getUserById(uuidUser);
        logger.info("ProductService:  productChange получили пользователя: " + user.getFirstName() + " " + user.getLastName());
        EnumCurrency currency = currencyService.getById(productChangeDTO.getUuidCurrency());
        logger.info("ProductService:  productChange получили курс: " + currency.getDescription());
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService:  productChange получили торговую точку: " + tradePoint.getName());
        Category category = categoryRepository.findById(productChangeDTO.getUuidCategory()).orElseThrow(() ->
                new CategoryNotFoundException("CategoryService: ---getCategoryById Такой категории не найдено"));
        logger.info("ProductService:  productChange получили категорию: " + category.getName());
        Subcategory subcategory = subcategoryRepository.findById(productChangeDTO.getUuidSubcategory()).orElseThrow(() ->
                new SubcategoryNotFoundException("SubcategoryService: ---getSubcategoryByUuid " +
                        "подкатегория по UUID не найдена в БД"));
        logger.info("ProductService:  productChange получили подкатегорию если имеется: " +
                Optional.ofNullable(subcategory.getName()).orElse("null"));
        CategorySubcategory categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category, subcategory);
        logger.info("ProductService:  productChange получили связь категория - подкатегория: " +
                categorySubcategory.getCategory().getName() + " " +
                Optional.ofNullable(categorySubcategory.getSubcategory().getName()).orElse("null"));
//        logger.info("ProductService:  productChange изменяем продукт : "
//                + product.getBrand() + "/n"
//                + product.getModel() + "/n"
//                + product.getCharacteristics() + "/n"
//                + product.getQuantity() + "/n"
//                + product.getCurrency().getDescription() + "/n"
//                + product.getCurrency().getRate() + "/n"
//                + product.getPriceWithVat() + "/n"
//                + product.getPriceSelling() + "/n"
//                + product.getStatusProduct().getStatus() + "/n"
//                + product.getDateOfArrival() + "/n"
//                + product.getUserAcceptedProduct() + "/n"
//                + product.getUserSaleProduct() + "/n"
//                + product.getCategorySubcategory().getCategory() + "/n"
//                + product.getCategorySubcategory().getSubcategory() + "/n"
//                + product.getTradePoint().getName() + "/n"
//                + product.getSku())
//        ;
        logger.info("ProductService:  productChange прежде чем изменять сохраняем в базу новые значения для " +
                "продукта в случае отката ");
        productChangeRequestService.save(productChangeDTO, product, user, tradePoint, currency, categorySubcategory);
        setStatusProductByNameAndSave(STATUS_PRODUCT_PENDING_APPROVAL, product);
    }

    @Transactional
    public Product setStatusProductByNameAndSave(String nameStatus, Product product) {
        logger.info("ProductService:  setStatusProductByNameAndSave " +
                "после сохранения устанавливаем статус продукту : PENDING_APPROVAL");
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый: " + product.getSku());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый: " + product);
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getVersion(): "
                + product.getVersion());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getStatusProduct(): "
                + product.getStatusProduct());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getId(): "
                + product.getId());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getCurrency().getDescription(): "
                + product.getCurrency().getDescription());
        EnumStatusProduct statusProduct = statusProductService.findByName(nameStatus);
        product.setStatusProduct(statusProduct);
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый: " + product.getSku());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый: " + product);
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getVersion(): "
                + product.getVersion());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getStatusProduct(): "
                + product.getStatusProduct());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getId(): "
                + product.getId());
        logger.info("ProductService:  setStatusProductByNameAndSave получили прорукт изменяемый product.getCurrency().getDescription(): "
                + product.getCurrency().getDescription());
        return productRepository.save(product);
    }

    //написать метод который возвращает список новых товаров в заявки
    @Transactional
    public List<ResponseProductDTO> getNewProductsDTOForApplications() {
        EnumStatusProduct statusProduct = statusProductService.findByName(STATUS_PRODUCT_PENDING_APPROVAL);
        List<Product> products = productRepository.findByStatusProduct(statusProduct);
        List<ResponseProductDTO> productDTOS = products.stream()
                .map(product -> toInResponseProductDTO(product))
                .collect(Collectors.toList());
        return productDTOS;
    }

}
