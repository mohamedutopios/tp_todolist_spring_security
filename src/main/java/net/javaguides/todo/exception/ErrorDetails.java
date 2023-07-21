package net.javaguides.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// class qui va servir à structurer les informations concernant les execptions personnalisés qui seront levées dans l'application.
public class ErrorDetails {
    // Champ pour stocker la date et l'heure du moment où l'erreur s'est produite.
    private LocalDateTime timeStamp;

    // Champ pour stocker le message décrivant l'erreur.
    private String message;

    // Champ pour stocker les détails supplémentaires de l'erreur.
    private String details;
}
