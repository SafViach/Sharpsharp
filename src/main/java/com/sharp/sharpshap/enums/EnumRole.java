package com.sharp.sharpshap.enums;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sharp.sharpshap.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class EnumRole {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false , unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles" , cascade = CascadeType.ALL)
    @JsonIgnore //отключаю сериализацию user во избежание бесконечной рекурсии
    private Set<User> users;
}
