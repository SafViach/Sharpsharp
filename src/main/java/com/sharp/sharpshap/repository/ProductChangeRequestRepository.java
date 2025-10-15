package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.ProductChangeRequest;
import com.sharp.sharpshap.entity.TradePoint;
import com.sharp.sharpshap.entity.User;
import com.sharp.sharpshap.enums.EnumStatusProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ProductChangeRequestRepository extends JpaRepository<ProductChangeRequest, UUID> {
    List<ProductChangeRequest> findByStatusProductIn(List<EnumStatusProduct> statusProduct);
    List<ProductChangeRequest> findByStatusProductInAndTradePoint(List<EnumStatusProduct> statusProduct, TradePoint tradePoint);
}
