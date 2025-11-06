package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.*;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;

import java.time.LocalDateTime;

import com.sharp.sharpshap.exceptions.*;
import com.sharp.sharpshap.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final StatusProductService statusProductService;
    private final UserService userService;
    private final SubcategoryRepository subcategoryRepository; //  : (
    private final CategoryRepository categoryRepository; //  : (
    private final ProductRepository productRepository;
    private final TradePointService tradePointService;
    private final CurrencyService currencyService;
    private final ProductChangeRequestService productChangeRequestService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final String STATUS_PRODUCT_AVAILABLE = "AVAILABLE";
    private final String STATUS_PRODUCT_PENDING = "PENDING";
    private final String STATUS_PRODUCT_REMOVABLE = "REMOVABLE";
    private final String STATUS_PRODUCT_CANCEL = "CANCEL";
    private final String STATUS_PRODUCT_EXAMINATION = "EXAMINATION";
    private final String STATUS_PRODUCT_MOVING = "MOVING";
    private final int MAX_PAGE_SIZE_PAGEABLE = 25;
    private final int MIN_PAGE_SIZE_PAGEABLE = 1;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createdProduct(ProductCreateDTO productCreateDTO,
                               UUID uuidCategory,
                               UUID uuidSubcategory,
                               UUID uuidUser,
                               UUID uuidTradePoint) {
        logger.info("ProductService: ---createdProduct добавление продукта");
        Product product = new Product();

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

        EnumStatusProduct statusProduct = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);
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

    private int checkPageSize(int pageSize) {
        // Ограничиваем максимальный и минимальный размер, чтобы избежать DoS
        if (pageSize > MAX_PAGE_SIZE_PAGEABLE) pageSize = MAX_PAGE_SIZE_PAGEABLE;
        if (pageSize < MIN_PAGE_SIZE_PAGEABLE) pageSize = MIN_PAGE_SIZE_PAGEABLE;
        return pageSize;
    }

    @Transactional
    public ResponseProductSlice getProductsByUuidTradePoint(UUID uuidTradePoint,
                                                            UUID uuidProductAfter,
                                                            int pageSize) {
        pageSize = checkPageSize(pageSize);

        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        EnumStatusProduct status = statusProductService.findByName(STATUS_PRODUCT_AVAILABLE);
        logger.info("ProductService: --- getProductsByUuidTradePoints получаем список продуктов по торговой точке: "
                + tradePoint.getName() + " фильтруя по статусу " + STATUS_PRODUCT_AVAILABLE);

        Pageable pageable = PageRequest.of(0, pageSize, Sort.by("id").ascending());

        List<Product> products = productRepository.filterByTradePointAndStatusForPageable(status, tradePoint, uuidProductAfter, pageable);

        boolean hasNext = products.size() > pageSize;
        if (hasNext) {
            // Убираем лишний элемент
            products = products.subList(0, pageSize);
        }


        List<ResponseOfTheProductFoundDTO> productFoundDTO = products.stream()
                .map(product -> ResponseOfTheProductFoundDTO.toResponseOfTheProductFoundDTO(product))
                .toList();
        return new ResponseProductSlice(productFoundDTO, hasNext);
    }


    public Product getProductByUuid(UUID uuidProduct) {
        return productRepository.findById(uuidProduct).orElseThrow(() -> new ProductNotFoundException("Продукт по uuid не найден"));
    }

    public ResponseProductDTO getResponseProductDTOByUuid(UUID uuidProduct) {
        return ResponseProductDTO.toInResponseProductDTO(getProductByUuid(uuidProduct));
    }


    public ResponseProductSlice getProductsByUuidTradePointNoAssignmentTradePointForUser(UUID uuidTradePoint,
                                                                                         UUID uuidProductAfter,
                                                                                         int pageSize) {
        pageSize = checkPageSize(pageSize);

        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService: getProductsByUuidTradePointNoAssignmentTradePointForUser получение продуктов на точке: " +
                tradePoint.getName() + "без присвоения пользователю данной точки");
        // Запрашиваем на 1 элемент больше, чтобы определить hasNext
        Pageable pageable = PageRequest.of(0, pageSize + 1, Sort.by("id").ascending());
        EnumStatusProduct status = statusProductService.findByName(STATUS_PRODUCT_AVAILABLE);
        List<Product> products = productRepository.filterByTradePointAndStatusForPageable(status, tradePoint, uuidProductAfter, pageable);

        boolean hasNext = products.size() > pageSize;
        if (hasNext) {
            // Убираем лишний элемент
            products = products.subList(0, pageSize);
        }
        List<ResponseOfTheProductFoundDTO> productFoundDTO = products.stream()
                .map(product -> ResponseOfTheProductFoundDTO.toResponseOfTheProductFoundDTO(product))
                .toList();
        return new ResponseProductSlice(productFoundDTO, hasNext);
    }

    @Transactional
    public ResponseProductSlice searchByLine(RequestSearchByLineDTO requestSearchByLineDTO,
                                             UUID uuidTradePoint,
                                             UUID uuidProductAfter,
                                             int pageSize) {
        logger.info("ProductService: searchByLine ");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        EnumStatusProduct status = statusProductService.findByName(STATUS_PRODUCT_AVAILABLE);

        pageSize = checkPageSize(pageSize);


        String line = requestSearchByLineDTO.getLineSearch();

        String cleaner = line.trim().toLowerCase();
        String[] parts = cleaner.split("\\s+");
        logger.info("ProductService: searchByLine Начинаем поиск продукта по подстрокам из " + parts.length + " ед.");
        // Запрашиваем на 1 элемент больше, чтобы определить hasNext
        Pageable pageable = PageRequest.of(0, pageSize + 1, Sort.by("id").ascending());

        if (parts.length == 1) {
            logger.info("ProductService: searchByLine parts.length == 1 " + parts[0]);
            List<Product> products = productRepository.filterByTradePointAndStatusForSearchBySkuKeyword(
                    status,
                    tradePoint,
                    parts[0],
                    uuidProductAfter,
                    pageable);

            return correctionListProduct(products, pageSize);

        } else if (parts.length == 2) {
            logger.info("ProductService: searchByLine parts.length == 2 " + parts[0] + " " + parts[1]);
            List<Product> products = productRepository.filterByTradePointAndStatusForSearchByTwoParts(
                    status,
                    tradePoint,
                    parts[0],
                    parts[1],
                    uuidProductAfter,
                    pageable);

            return correctionListProduct(products, pageSize);

        } else if (parts.length == 3) {
            logger.info("ProductService: searchByLine parts.length == 3 " + parts[0] + " " + parts[1] + " " + parts[2]);
            List<Product> products = productRepository.filterByTradePointAndStatusForSearchByThreeParts(
                    status,
                    tradePoint,
                    parts[0],
                    parts[1],
                    parts[2],
                    uuidProductAfter,
                    pageable);

            return correctionListProduct(products, pageSize);

        } else if (parts.length == 4) {
            logger.info("ProductService: searchByLine parts.length == 4 " + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3]);
            List<Product> products = productRepository.filterByTradePointAndStatusForSearchByFourParts(
                    status,
                    tradePoint,
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    uuidProductAfter,
                    pageable);

            return correctionListProduct(products, pageSize);

        } else if (parts.length >= 5) {
            logger.info("ProductService: searchByLine parts.length == 5 " + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
            List<Product> products = productRepository.filterByTradePointAndStatusForSearchByFiveParts(
                    status,
                    tradePoint,
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    uuidProductAfter,
                    pageable);

            return correctionListProduct(products, pageSize);

        }
        logger.info("ProductService: searchByLine parts.length == 5 и больше" + parts[0] + " " + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4]);
        List<Product> products = productRepository.filterByTradePointAndStatusForSearchByFiveParts(
                status,
                tradePoint,
                parts[0],
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                uuidProductAfter,
                pageable);

        return correctionListProduct(products, pageSize);
    }

    private ResponseProductSlice correctionListProduct(List<Product> products, int pageSize) {
        boolean hasNext = false;
        hasNext = products.size() > pageSize;
        if (hasNext) {
            // Убираем лишний элемент
            products = products.subList(0, pageSize);
        }
        List<ResponseOfTheProductFoundDTO> productFoundDTO = products.stream()
                .map(product -> ResponseOfTheProductFoundDTO.toResponseOfTheProductFoundDTO(product))
                .toList();
        return new ResponseProductSlice(productFoundDTO, hasNext);
    }

    public void productChange(UUID uuidProduct,
                              UUID uuidUser,
                              UUID uuidTradePoint,
                              ProductChangeDTO productChangeDTO) {
        logger.info("ProductService:  productChange изменение продукта");
        Product product = getProductByUuid(uuidProduct);
        logger.info("ProductService:  productChange получили прорукт изменяемый: " + product.getSku());

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
                Optional.ofNullable(subcategory.getName()).orElse(" "));

        CategorySubcategory categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category, subcategory);
        logger.info("ProductService:  productChange получили связь категория - подкатегория: " +
                categorySubcategory.getCategory().getName() + " " +
                Optional.ofNullable(categorySubcategory.getSubcategory().getName()).orElse(" "));

        logger.info("ProductService:  productChange прежде чем изменять сохраняем в базу новые значения для " +
                "продукта в случае отката ");
        productChangeRequestService.save(productChangeDTO, product, user, tradePoint, currency, categorySubcategory);
        setStatusProductByNameAndSave(STATUS_PRODUCT_PENDING, product.getId());
    }

    @Transactional
    public Product setStatusProductByNameAndSave(String nameStatus,
                                                 UUID uuidProduct) {
        Product product = getProductByUuid(uuidProduct);
        EnumStatusProduct statusProduct = statusProductService.findByName(nameStatus);
        product.setStatusProduct(statusProduct);
        logger.info("ProductService:  setStatusProductByNameAndSave продукту: " + product.getSku() + " именяем статус "
                + product.getStatusProduct().getStatus() + " на " + nameStatus + " и сохраняем");

        return productRepository.save(product);
    }

    //написать метод который возвращает список новых товаров в заявки
    public List<ResponseProductDTO> getNewProductsDTOForApplications() {
        logger.info("ProductService:  getNewProductsDTOForApplications получение товаров статуса " +
                STATUS_PRODUCT_EXAMINATION + "- в ожидании одобрения");
        EnumStatusProduct statusProductExamination = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);
        List<EnumStatusProduct> statusProduct = List.of(statusProductExamination);
        List<Product> products = productRepository.findByStatusProductIn(statusProduct);
        List<ResponseProductDTO> productDTOS = products.stream()
                .map(product -> ResponseProductDTO.toInResponseProductDTO(product))
                .collect(Collectors.toList());
        return productDTOS;
    }

    //написать метод который возвращает список новых товаров в заявки для user
    public List<ResponseProductDTO> getApplicationsNewProductFilteredByTradePointAndStatusProductDTO(UUID uuidTradePoint) {
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        EnumStatusProduct statusProductCancel = statusProductService.findByName(STATUS_PRODUCT_CANCEL);
        EnumStatusProduct statusProductExamination = statusProductService.findByName(STATUS_PRODUCT_EXAMINATION);
        EnumStatusProduct statusProductMoving = statusProductService.findByName(STATUS_PRODUCT_MOVING);
        List<EnumStatusProduct> statusProduct = List.of(statusProductCancel, statusProductExamination, statusProductMoving);
        logger.info("ProductService:  getApplicationsNewProductFilteredByTradePointAndStatusProductDTO получение " +
                "товаров статуса: " + STATUS_PRODUCT_CANCEL + " + " + STATUS_PRODUCT_EXAMINATION + " + "
                + STATUS_PRODUCT_MOVING + " соответствующей точке: " + tradePoint.getName()
        );

        List<Product> products = productRepository.findByStatusProductInAndTradePoint(statusProduct, tradePoint);
        List<ResponseProductDTO> productDTOS = products.stream()
                .map(product -> ResponseProductDTO.toInResponseProductDTO(product))
                .collect(Collectors.toList());
        return productDTOS;
    }

    public void acceptNewProduct(UUID uuidProduct) {
        logger.info("ProductService:  acceptNewProduct товар одобрен администратором");
        setStatusProductByNameAndSave(STATUS_PRODUCT_AVAILABLE, uuidProduct);
    }

    //создать метод для администратора который при отказе добавления нового продукта пользователем изменят статус
    //продукту на "CANCEL"
    public void cancelAddNewProduct(UUID uuidProduct) {
        logger.info("ProductService:  cancelAddNewProduct товар");
        setStatusProductByNameAndSave(STATUS_PRODUCT_CANCEL, uuidProduct);
    }

    @Transactional
    public Product sendToAnotherTradePoint(UUID uuidProduct, UUID uuidTradePoint) {
        Product product = getProductByUuid(uuidProduct);
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);

        if (tradePoint.getName().equals(product.getTradePoint().getName())) {
            throw new ProductException("ошибка : данный продукт принадележит данной точке");
        }
        logger.info("ProductService:  sendToAnotherTradePoint отправляем продукт : " + product.getSku()
                + " на торговую точку: " + tradePoint.getName());

        return setStatusProductByNameAndSave(STATUS_PRODUCT_PENDING, product.getId());
    }


    @Transactional
    public void deleteProduct(UUID uuidProduct) {
        Product product = getProductByUuid(uuidProduct);
        checkStatusProduct(product);
        logger.info("ProductService:  deleteProduct удаление продукта: " + product.getSku());
        productRepository.delete(product);
    }

    private void checkStatusProduct(Product product) {
        if (!product.getStatusProduct().getStatus().equals(STATUS_PRODUCT_CANCEL) ||
                !product.getStatusProduct().equals(STATUS_PRODUCT_EXAMINATION) ||
                !product.getStatusProduct().equals(STATUS_PRODUCT_REMOVABLE)) {
            throw new ProductChangeRequestException("Продукт не равен статусу: " + STATUS_PRODUCT_CANCEL + " , " +
                    STATUS_PRODUCT_EXAMINATION + " , " + STATUS_PRODUCT_REMOVABLE);
        }

    }

    public void setTradePoint(Product product, UUID uuidTradePoint) {
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);
        logger.info("ProductService:  setTradePoint товар прибыл на точку:" + tradePoint.getName());
        if (product.getStatusProduct().equals(STATUS_PRODUCT_MOVING)) {
            product.setTradePoint(tradePoint);
        } else {
            throw new ProductException("Товар не равен статусу : " + STATUS_PRODUCT_MOVING + "точка не присвоена");
        }
        setStatusProductByNameAndSave(STATUS_PRODUCT_AVAILABLE, product.getId());
    }

}
