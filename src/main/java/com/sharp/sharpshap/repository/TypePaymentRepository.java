package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.enums.EnumTypePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TypePaymentRepository extends JpaRepository<EnumTypePayment, UUID> {

}
