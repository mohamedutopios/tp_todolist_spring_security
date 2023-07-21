package net.javaguides.todo.security;


import net.javaguides.todo.entity.User;
import net.javaguides.todo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

// L'annotation @Service est utilisée pour indiquer que cette classe est un service Spring géré par le conteneur IoC.
// Elle permet de découvrir automatiquement cette classe et de l'instancier en tant que service.
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Injection de dépendance de l'interface UserRepository.
    // Cette dépendance est automatiquement résolue par Spring car UserRepository est également un composant Spring.
    // L'injection de dépendance permet d'accéder à la couche d'accès aux données (repository) pour récupérer les détails de l'utilisateur.
    private UserRepository userRepository;

    // Cette méthode est implémentée à partir de l'interface UserDetailsService.
    // Elle est utilisée pour charger les détails de l'utilisateur (UserDetails) en fonction du nom d'utilisateur (ou de l'email) fourni.
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        // Recherche de l'utilisateur dans la base de données en utilisant le nom d'utilisateur ou l'email fourni.
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                // Si l'utilisateur n'est pas trouvé, une exception UsernameNotFoundException est levée.
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));

        // Crée un ensemble d'objets GrantedAuthority à partir des rôles de l'utilisateur.
        // Les GrantedAuthority représentent les autorisations accordées à un utilisateur (par exemple, rôle "ADMIN" ou "USER").
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // Retourne un objet UserDetails (implémenté par la classe User de Spring Security) qui représente l'utilisateur.
        // Cet objet UserDetails est utilisé par Spring Security pour gérer l'authentification et l'autorisation de l'utilisateur.
        // Il contient des informations telles que le nom d'utilisateur, le mot de passe et les autorisations (rôles) de l'utilisateur.
        return new org.springframework.security.core.userdetails.User(
                usernameOrEmail, // Nom d'utilisateur (ou email) de l'utilisateur.
                user.getPassword(), // Mot de passe de l'utilisateur.
                authorities // Liste des autorisations (rôles) de l'utilisateur.
        );
    }
}
