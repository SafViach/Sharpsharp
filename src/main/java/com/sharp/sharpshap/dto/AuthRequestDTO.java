package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank(message = "Введите логин")
    private String login;
    @NotEmpty(message = "Введите пароль")
    private String password;
}
