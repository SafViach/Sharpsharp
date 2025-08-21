package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    public Optional<Discount> findByDiscountAmount(BigDecimal discountAmount);
}
