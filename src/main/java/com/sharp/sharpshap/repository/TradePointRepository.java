package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.TradePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradePointRepository extends JpaRepository<TradePoint, Integer> {
}
