package net.javaguides.todo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.javaguides.todo.exception.TodoAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

//Avec cette class, On va pouvoir créer et vérifier des jetons et aussi récupérer un username par le jeton.
@Component
public class JwtTokenProvider {

    // L'annotation @Value est utilisée pour injecter les valeurs des propriétés de configuration définies dans les fichiers de propriétés(applications.properties).
    // Dans ce cas, les propriétés "app.jwt-secret" et "app.jwt-expiration-milliseconds" seront injectées depuis les fichiers de configuration.
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Méthode pour générer un jeton JWT à partir des informations d'authentification fournies.
    // Elle prend un objet d'authentification (Authentication) en tant que paramètre, à partir duquel elle extrait le nom d'utilisateur.
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        // Génère le jeton JWT en utilisant la bibliothèque JJWT (Java JWT).
        // Elle utilise le nom d'utilisateur comme sujet, la date actuelle comme date d'émission (issuedAt),
        // la date d'expiration (expireDate) et une clé secrète pour signer le jeton.
        // La méthode compact() génère la chaîne du jeton JWT.
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

    // Méthode privée pour créer la clé de signature à partir de la clé secrète JWT (jwtSecret).
    // Elle décode la clé secrète Base64 et la retourne comme une clé HMAC (clé de signature).
    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Méthode pour obtenir le nom d'utilisateur à partir du jeton JWT.
    public String getUsername(String token) {
        // Parse le jeton JWT pour obtenir les revendications (claims).
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Obtient le nom d'utilisateur à partir des revendications.
        String username = claims.getSubject();

        return username;
    }

    // Méthode pour valider si le jeton JWT est valide.
    public boolean validateToken(String token) {
        // Parse le jeton JWT pour vérifier sa validité.
        // Si le jeton est invalide (par exemple, la signature est incorrecte ou le jeton a expiré),
        // une exception sera levée par la méthode parse(), indiquant que le jeton n'est pas valide.
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (JwtException e) {
            // En cas d'erreur de validation du jeton (JwtException), la méthode renverra false.
            return false;
        }
    }
}

