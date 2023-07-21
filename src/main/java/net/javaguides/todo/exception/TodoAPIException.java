package net.javaguides.todo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
// Cette class va permettre de personnalisé des execptions génériques dans notre application.
public class TodoAPIException extends RuntimeException {

    // Champ pour stocker le code de statut HTTP associé à l'exception.
    private HttpStatus status;

    // Champ pour stocker le message décrivant l'exception.
    private String message;
}
