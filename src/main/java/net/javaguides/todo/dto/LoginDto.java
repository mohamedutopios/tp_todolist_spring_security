package net.javaguides.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Ce DTO va servir à stocker lors de la requête de login les informations d'authentification.
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}
