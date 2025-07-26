package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.TradePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TradePointRepository extends JpaRepository<TradePoint, UUID> {
    Optional<TradePoint> findByName(String name);
}
