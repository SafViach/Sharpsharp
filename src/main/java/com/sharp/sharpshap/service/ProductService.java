package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;

import java.time.LocalDateTime;

import com.sharp.sharpshap.exceptions.CategoryNotFoundException;
import com.sharp.sharpshap.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.*;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategorySubcategoryService categorySubcategoryService;
    private final DiscountService discountService;
    private final StatusProductService statusProductService;
    private final UserService userService;
    private final SubcategoryService subcategoryService;
    private final CategoryRepository categoryRepository; //  : (
    private final ProductRepository productRepository;
    private final TradePointService tradePointService;
    private final CurrencyService currencyService;
    private final static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
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
            logger.info("ProductService: ---createdProduct subcategoryService.getSubcategoryByUuid(uuidSubcategory);");
            subcategory = subcategoryService.getSubcategoryByUuid(uuidSubcategory);
        }


        logger.info("ProductService: ---createdProduct userService.getUserById(uuidUser);");
        User user = userService.getUserById(uuidUser);

        logger.info("ProductService: ---createdProduct currencyService.getById(productCreateDTO.getCurrencyId());");
        EnumCurrency currency = currencyService.getById(productCreateDTO.getCurrencyId());

        logger.info("ProductService: ---createdProduct tradePointService.getByIdTradePoint(uuidTradePoint);");
        TradePoint tradePoint = tradePointService.getByIdTradePoint(uuidTradePoint);

        logger.info("ProductService: ---createdProduct statusProductService.findByName(\"EXAMINATION\");");
        EnumStatusProduct statusProduct = statusProductService.findByName("EXAMINATION");

        CategorySubcategory categorySubcategory = new CategorySubcategory();

        logger.info("ProductService: ---createdProduct .setBrand(productCreateDTO.getBrand());");
        product.setBrand(productCreateDTO.getBrand());

        logger.info("ProductService: ---createdProduct .setModel(productCreateDTO.getModel());");
        product.setModel(productCreateDTO.getModel());

        logger.info("ProductService: ---createdProduct .setCharacteristics(productCreateDTO.getCharacteristics());");
        product.setCharacteristics(productCreateDTO.getCharacteristics());

        logger.info("ProductService: ---createdProduct .setQuantity(productCreateDTO.getQuantity());");
        product.setQuantity(productCreateDTO.getQuantity());

        logger.info("ProductService: ---createdProduct .setCurrency(currency);");
        product.setCurrency(currency);

        logger.info("ProductService: ---createdProduct .setPriceWithVat(productCreateDTO.getPriceWithVat());");
        product.setPriceWithVat(productCreateDTO.getPriceWithVat());

        logger.info("ProductService: ---createdProduct .setPriceSelling(productCreateDTO.getPriceSelling());");
        product.setPriceSelling(productCreateDTO.getPriceSelling());

        logger.info("ProductService: ---createdProduct .setStatusProduct(statusProduct);");
        product.setStatusProduct(statusProduct);

        logger.info("ProductService: ---createdProduct .setDateOfArrival(LocalDateTime.now());");
        product.setDateOfArrival(LocalDateTime.now());

        logger.info("ProductService: ---createdProduct .setUserAcceptedProduct(user);");
        product.setUserAcceptedProduct(user);

        logger.info("ProductService: ---createdProduct .setTradePoint(tradePoint);");
        product.setTradePoint(tradePoint);

        logger.info("ProductService: ---createdProduct .setDiscount(discount);");
        product.setDiscount(discount);


        try {
            logger.info("ProductService: ---createdProduct categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category, subcategory);");
            categorySubcategory = categorySubcategoryService.getByCategoryAndSubcategory(category, subcategory);
        } catch (IncorrectResultSizeDataAccessException e) {
            logger.error("в categorySubcategory содержится боллее одной одинаковой записи");
        }


        logger.info("ProductService: ---createdProduct .setCategorySubcategory(categorySubcategory);");
        product.setCategorySubcategory(categorySubcategory);

        product.setSku(generationFieldSku(
                productCreateDTO.getBrand(),
                productCreateDTO.getModel(),
                productCreateDTO.getCharacteristics()));

        logger.info("ProductService: ---createdProduct return productRepository.save(product);");

        product.setPriceSelling(calcTheSaleProduct(productCreateDTO,category,subcategory));
        productRepository.save(product);
    }

    public BigDecimal calcTheSaleProduct(ProductCreateDTO productCreateDTO ,Category category , Subcategory subcategory){
        BigDecimal rateCurrency = productCreateDTO.getRateCurrency();
        BigDecimal priceWithVat = productCreateDTO.getPriceWithVat();

        BigDecimal sumPriceProductNotCoefficient = rateCurrency.multiply(priceWithVat);
        BigDecimal sumPriceProduct;

        if(subcategory != null){
            BigDecimal coeff = subcategory.getCoefficientSales();
            sumPriceProduct = coeff.multiply(sumPriceProductNotCoefficient);
        }else{
            BigDecimal coeff = category.getCoefficientSale();
            sumPriceProduct = coeff.multiply(sumPriceProductNotCoefficient);

        }
        return toDot90(sumPriceProduct);
    }
    public BigDecimal toDot90(BigDecimal price) {
        // Округляем до целого вниз (берём целую часть)
        BigDecimal wholePart = price.setScale(0, BigDecimal.ROUND_HALF_UP);//RoundingMode.FLOOR);
        // Добавляем 0.90
        return wholePart.add(new BigDecimal("0.90"));
    }

    public String generationFieldSku(String brand, String model, String characteristics) {
        return String.join("-", brand, model, characteristics).toUpperCase();
    }
    public boolean checkCategoryForProducts(Category category){
        return productRepository.existsByCategorySubcategory_Category_Id(category.getId());
    }
    public boolean checkSubcategoryForProducts(Subcategory subcategory){
        return productRepository.existsByCategorySubcategory_Subcategory_Id(subcategory.getId());
    }
}
