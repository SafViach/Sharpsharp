package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.TariffPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TariffPlanRepository extends JpaRepository<TariffPlan, UUID> {
    Optional<TariffPlan> findByName(String name);

    @Query("""
            SELECT t FROM TariffPlan t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<TariffPlan> findByKeyword(String keyword);

    @Query("""
            SELECT t FROM TariffPlan t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :part1, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part2, '%'))
            """)
    List<TariffPlan> findByKeywordTwoParts(@Param("part1") String part1,
                                           @Param("part2") String part2);

    @Query("""
            SELECT t FROM TariffPlan t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :part1, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part2, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part3, '%'))
            """)
    List<TariffPlan> findByKeywordThreeParts(@Param("part1") String part1,
                                             @Param("part2") String part2,
                                             @Param("part3") String part3);

    @Query("""
            SELECT t FROM TariffPlan t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :part1, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part2, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part3, '%'))
            AND LOWER(t.name) LIKE LOWER(CONCAT('%', :part4, '%'))
            """)
    List<TariffPlan> findByKeywordFourParts(@Param("part1") String part1,
                                            @Param("part2") String part2,
                                            @Param("part3") String part3,
                                            @Param("part4") String part4);

    boolean existsByNameAndPrice(String name, BigDecimal price);
}
