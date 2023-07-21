package net.javaguides.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

// L'annotation @ControllerAdvice est utilisée pour définir une classe qui fournit des méthodes de gestion
// globales pour les exceptions qui se produisent dans l'application.
@ControllerAdvice
public class GlobalExceptionHandler {

    // L'annotation @ExceptionHandler indique que cette méthode est utilisée pour gérer les exceptions
    // de type TodoAPIException. Si une TodoAPIException est lancée dans l'application, cette méthode sera appelée.
    @ExceptionHandler(TodoAPIException.class)
    public ResponseEntity<ErrorDetails> handleTodoAPIException(TodoAPIException exception,
                                                               WebRequest webRequest) {
        // Crée une instance de ErrorDetails pour encapsuler les détails de l'erreur.
        // Cette classe est utilisée pour renvoyer des détails sur les erreurs aux clients de l'API.
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(), // Date et heure actuelles où l'erreur s'est produite.
                exception.getMessage(), // Le message décrivant l'erreur de l'exception TodoAPIException.
                webRequest.getDescription(false) // Description de la requête où l'erreur s'est produite (paramètres, URL, etc.).
        );

        // Retourne une réponse HTTP avec l'objet ErrorDetails contenant les détails de l'erreur et le code de statut "400 BAD REQUEST".
        // Dans ce cas, le code de statut indique une mauvaise requête car une exception TodoAPIException a été levée.
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}

