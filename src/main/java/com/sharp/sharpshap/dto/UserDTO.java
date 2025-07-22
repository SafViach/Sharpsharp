package com.sharp.sharpshap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NotBlank(message = "Логин работника не может быть пустым")
    @Size(min = 2 , max = 50 , message = "Логин работника должно быть от 2-х до 50-ти символов")
    private String login;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Пароль работника не может быть пустым")
    private String password;
}
