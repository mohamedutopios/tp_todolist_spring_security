package net.javaguides.todo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Injection de dépendance de la class JwtTokenProvider.
    // L'injection de dépendance permet d'accéder à la logique de gestion des jetons JWT.
    //On va pouvoir créer et vérifier des jetons et aussi récupérer un username par le jeton.
    // class qui se trouve dans security
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Injection de dépendance de l'interface UserDetailsService.
    // Cette dépendance est automatiquement résolue par Spring car UserDetailsService est également un composant Spring.
    // Elle est utilisée pour charger les détails de l'utilisateur (UserDetails) en fonction du nom d'utilisateur (ou de l'email) fourni.
    @Autowired
    private UserDetailsService userDetailsService;

    // Constructeur de la classe JwtAuthenticationFilter.
    // Les dépendances JwtTokenProvider et UserDetailsService sont passées en tant que paramètres.
    // Ces dépendances sont automatiquement injectées lors de la création de l'instance de cette classe par Spring.
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    // Cette méthode est exécutée pour chaque requête entrante une seule fois (OncePerRequestFilter).
    // Elle est utilisée pour filtrer les requêtes et gérer l'authentification basée sur les jetons JWT.
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Récupère le jeton JWT à partir de l'en-tête "Authorization" de la requête.
        String token = getTokenFromRequest(request);

        // Vérifie si le jeton est valide en utilisant la logique de validation fournie par JwtTokenProvider.
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

            // Récupère le nom d'utilisateur à partir du jeton JWT.
            String username = jwtTokenProvider.getUsername(token);

            // Charge les détails de l'utilisateur (UserDetails) en utilisant le nom d'utilisateur récupéré.
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Crée un objet UsernamePasswordAuthenticationToken pour représenter l'authentification de l'utilisateur.
            // Cet objet est utilisé par Spring Security pour gérer l'authentification de l'utilisateur.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, // UserDetails représentant l'utilisateur authentifié.
                    null, // Mot de passe (n'est pas nécessaire car nous utilisons des jetons JWT).
                    userDetails.getAuthorities() // Liste des autorisations (rôles) de l'utilisateur.
            );

            // Définit les détails d'authentification de la requête, tels que l'adresse IP et les en-têtes.
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Définit l'objet UsernamePasswordAuthenticationToken comme l'authentification actuelle dans le contexte de sécurité.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Passe la requête et la réponse au prochain filtre de la chaîne de filtres.
        filterChain.doFilter(request, response);
    }

    // Méthode pour récupérer le jeton JWT à partir de l'en-tête "Authorization" de la requête.
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }

        return null;
    }
}
