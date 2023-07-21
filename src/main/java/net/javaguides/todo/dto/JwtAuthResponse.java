package net.javaguides.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    // Champ pour stocker le jeton d'accès JWT.
    private String accessToken;

    // Champ pour stocker le type de jeton (dans ce cas, c'est "Bearer").
    // Par défaut, le type de jeton est défini sur "Bearer" pour être conforme aux normes JWT.
    private String tokenType = "Bearer";
}
