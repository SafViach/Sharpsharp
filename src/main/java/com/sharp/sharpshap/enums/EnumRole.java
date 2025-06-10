package com.sharp.sharpshap.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharp.sharpshap.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
public class EnumRole {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore //отключаю сериализацию user во избежание бесконечной рекурсии
    private List<User> users = new ArrayList<>();
}
