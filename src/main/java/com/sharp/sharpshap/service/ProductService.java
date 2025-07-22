package com.sharp.sharpshap.service;

import com.sharp.sharpshap.dto.ProductCreateDTO;
import com.sharp.sharpshap.entity.*;
import com.sharp.sharpshap.enums.EnumCurrency;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import com.sharp.sharpshap.enums.StatusProduct;
import com.sharp.sharpshap.mappers.ProductMapper;
import com.sharp.sharpshap.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final StatusProductRepository statusProductRepository;
    private final TradePointRepository tradePointRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final CategorySubcategoryRepository categorySubcategoryRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    @Transactional
    public Product createdProduct(ProductCreateDTO productCreateDTO, UUID categoryUUID,
                                  UUID subcategoryUUID, UUID currencyUUID) {

        Product product = productMapper.toEntity(productCreateDTO);

        EnumStatusProduct statusProduct = statusProductRepository.findByStatus("EXAMINATION").get();
        System.out.println(statusProduct);
        TradePoint tradePoint = tradePointRepository.findByName("Хатаевича").get();
        System.out.println(tradePoint);
        Category category = categoryRepository.findById(categoryUUID).get();
        System.out.println(category);
        Subcategory subcategory = subcategoryRepository.findById(subcategoryUUID).get();
        System.out.println(subcategory);

        CategorySubcategory catSub = new CategorySubcategory();
        catSub.setCategory(category);
        catSub.setSubcategory(subcategory);
        System.out.println(catSub);
        categorySubcategoryRepository.save(catSub);

        CategorySubcategory categorySubcategory = categorySubcategoryRepository.
                findByCategoryIdAndSubcategoryId(categoryUUID, subcategoryUUID).get();
        System.out.println(catSub);
        EnumCurrency currency = currencyRepository.findById(currencyUUID).get();
        User user = userRepository.findByFirstName("Slava").get();
        System.out.println(user);
        BigDecimal two = new BigDecimal(2);

        product.setCurrency(currency);
        product.setStatusProduct(statusProduct);
        product.setTradePoint(tradePoint);
        product.setCategorySubcategory(categorySubcategory);
        product.setDateOfArrival(LocalDateTime.now());
        product.setUserAcceptedProduct(user);
        product.setPriceSelling(product.getPriceWithVat().multiply(two));
        return productRepository.save(product);


    }
}
