package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.enums.EnumRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<EnumRole, Integer> {
    Optional<EnumRole> findByName(String name);
}
