package com.sharp.sharpshap.entity;

import com.sharp.sharpshap.enums.EnumRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Имя работника не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя работника должно быть от 2-х до 50-ти символов")
    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String firstName;

    @Column(unique = true, nullable = false)
    private String lastName;

    private double salary;

    @NotBlank(message = "Пароль работника не может быть пустым")
    private String password;

    private boolean enable = true;
    private boolean accountNonLocked = true;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<EnumRole> roles;

}