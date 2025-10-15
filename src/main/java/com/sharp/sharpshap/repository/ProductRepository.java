package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Product;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
            SELECT p FROM Product p
            WHERE (:afterId IS NULL OR p.id > :afterId)
            AND p.statusProduct = :status
            AND p.tradePoint = :tradePoint
            AND LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY p.id
            """)
    List<Product> filterByTradePointAndStatusForSearchBySkuKeyword(@Param("status") EnumStatusProduct statusProduct,
                                                                   @Param("tradePoint") TradePoint tradePoint,
                                                                   @Param("keyword") String keyword,
                                                                   @Param("afterId") UUID afterId,
                                                                   Pageable pageable);

    @Query("""
            SELECT p FROM Product p
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND p.statusProduct = :status
            AND p.tradePoint = :tradePoint
            AND LOWER(p.sku) LIKE LOWER(CONCAT('%', :part1, '%'))
            AND LOWER(p.sku) LIKE LOWER(CONCAT('%', :part2, '%'))
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForSearchByTwoParts(@Param("status") EnumStatusProduct statusProduct,
                                                                 @Param("tradePoint") TradePoint tradePoint,
                                                                 @Param("part1") String part1,
                                                                 @Param("part2") String part2,
                                                                 @Param("afterId") UUID afterId,
                                                                 Pageable pageable);

    @Query("""
            SELECT p FROM Product p 
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND  p.statusProduct = :status
            AND p.tradePoint = :tradePoint
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part1, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%'))
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForSearchByThreeParts(@Param("status") EnumStatusProduct statusProduct,
                                                                   @Param("tradePoint") TradePoint tradePoint,
                                                                   @Param("part1") String part1,
                                                                   @Param("part2") String part2,
                                                                   @Param("part3") String part3,
                                                                   @Param("afterId") UUID afterId,
                                                                   Pageable pageable);

    @Query("""
            SELECT p FROM Product p 
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND  p.statusProduct = :status
            AND p.tradePoint = :tradePoint
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part1, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%')) 
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part4, '%'))
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForSearchByFourParts(@Param("status") EnumStatusProduct statusProduct,
                                                                  @Param("tradePoint") TradePoint tradePoint,
                                                                  @Param("part1") String part1,
                                                                  @Param("part2") String part2,
                                                                  @Param("part3") String part3,
                                                                  @Param("part4") String part4,
                                                                  @Param("afterId") UUID afterId,
                                                                  Pageable pageable);

    @Query("""
            SELECT p FROM Product p 
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND  p.statusProduct = :status
            AND p.tradePoint = :tradePoint 
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part1, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part2, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part3, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part4, '%'))
            AND LOWER(p.sku) LIKE LOWER (CONCAT('%', :part5, '%'))
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForSearchByFiveParts(@Param("status") EnumStatusProduct statusProduct,
                                                                  @Param("tradePoint") TradePoint tradePoint,
                                                                  @Param("part1") String part1,
                                                                  @Param("part2") String part2,
                                                                  @Param("part3") String part3,
                                                                  @Param("part4") String part4,
                                                                  @Param("part5") String part5,
                                                                  @Param("afterId") UUID afterId,
                                                                  Pageable pageable);


    List<Product> findByStatusProductIn(List<EnumStatusProduct> statusProduct);

    List<Product> findByStatusProductAndTradePoint(EnumStatusProduct statusProduct, TradePoint tradePoint);

    @Query("""
            SELECT p FROM Product p 
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND  p.statusProduct = :status
            AND p.tradePoint = :tradePoint
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForPageable(@Param("status") EnumStatusProduct status,
                                                         @Param("tradePoint") TradePoint tradePoint,
                                                         @Param("afterId") UUID afterId,
                                                         Pageable pageable);

    @Query("""
            SELECT p FROM Product p 
            WHERE (:afterId IS NULL OR p.id > :afterId) 
            AND  p.statusProduct = :status 
            AND p.tradePoint = :tradePoint 
            AND p.id > :afterId
            ORDER BY p.id ASC
            """)
    List<Product> filterByTradePointAndStatusForFindNextPage(@Param("status") EnumStatusProduct status,
                                                             @Param("tradePoint") TradePoint tradePoint,
                                                             @Param("afterId") UUID afterId,
                                                             Pageable pageable);

    List<Product> findByStatusProductInAndTradePoint(List<EnumStatusProduct> statusProduct, TradePoint tradePoint);


    boolean existsByCategorySubcategory_Category_Id(UUID uuidCategory);

    boolean existsByCategorySubcategory_Subcategory_Id(UUID uuidSubcategory);
}
