package com.sharp.sharpshap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sharp.sharpshap.enums.EnumRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"user\"")

public class User{
    @Id
    @GeneratedValue
    private UUID id;
    @NotBlank(message = "Имя работника не может быть пустым")
    @Size(min = 2 , max = 50 , message = "Имя работника должно быть от 2-х до 50-ти символов")
    @Column(unique = true)
    private String login;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private double salary;
    @NotBlank(message = "Пароль работника не может быть пустым")
    private String password;   
    @Column(nullable = false)
    private boolean enable;
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<EnumRole> roles = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "trade_point_id")
    private TradePoint tradePointId;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}