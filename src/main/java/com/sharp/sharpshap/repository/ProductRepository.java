package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchBySkuKeyword(@Param("keyword") String keyword);


    @Query("SELECT p FROM Product p WHERE LOWER(p.sku) LIKE " +
            "LOWER (CONCAT('%', :part1, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%'))")
    List<Product> searchByTwoParts(@Param("part1") String part1, @Param("part2") String part2);

    @Query("SELECT p FROM Product p WHERE LOWER(p.sku) LIKE " +
            "LOWER (CONCAT('%', :part1, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%'))")
    List<Product> searchByThreeParts(@Param("part1") String part1, @Param("part2") String part2, @Param("part3") String part3);

    @Query("SELECT p FROM Product p WHERE LOWER(p.sku) LIKE " +
            "LOWER (CONCAT('%', :part1, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part4, '%'))")
    List<Product> searchByFourParts(@Param("part1") String part1,
                                    @Param("part2") String part2,
                                    @Param("part3") String part3,
                                    @Param("part4") String part4);

    @Query("SELECT p FROM Product p WHERE LOWER(p.sku) LIKE " +
            "LOWER (CONCAT('%', :part1, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part4, '%')) AND " +
            "LOWER(p.sku) LIKE LOWER (CONCAT('%', :part5, '%'))")
    List<Product> searchByFiveParts(@Param("part1") String part1,
                                    @Param("part2") String part2,
                                    @Param("part3") String part3,
                                    @Param("part4") String part4,
                                    @Param("part5") String part5);

    Optional<Product> findBySku(String sku);

    List<Product> findByStatusProduct(EnumStatusProduct statusProduct);

    List<Product> findByTradePointAndStatusProduct(TradePoint tradePoint, EnumStatusProduct statusProduct);

    boolean existsByCategorySubcategory_Category_Id(UUID uuidCategory);

    boolean existsByCategorySubcategory_Subcategory_Id(UUID uuidSubcategory);
}
