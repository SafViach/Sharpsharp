package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.enums.EnumMoneyLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EnumMoneyLocationRepository extends JpaRepository<EnumMoneyLocation, UUID> {

}
