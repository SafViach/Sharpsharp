package com.sharp.sharpshap.repository;

import com.sharp.sharpshap.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u WHERE LOWER(u.lastName) LIKE  lOWER(CONCAT(:name,'%')) OR lOWER(u.firstName) LIKE" +
            " LOWER(CONCAT(:name,'%'))")
    List<User> findByLastNameOrFirstName(@Param("name") String prefix);

    Optional<User> findByLastName(String lastName);

    Optional<User> findByFirstName(String firstName);
} 