package net.javaguides.todo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Cette méthode est implémentée à partir de l'interface AuthenticationEntryPoint.
    // Elle est utilisée pour commencer le processus d'authentification lorsque l'accès à une ressource est refusé à un utilisateur non authentifié.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Cette méthode envoie une réponse d'erreur HTTP avec le code de statut "UNAUTHORIZED" (401).
        // Le message d'erreur est récupéré à partir de l'exception d'authentification (AuthenticationException).
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}

