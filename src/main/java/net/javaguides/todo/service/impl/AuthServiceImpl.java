package net.javaguides.todo.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.todo.dto.LoginDto;
import net.javaguides.todo.dto.RegisterDto;
import net.javaguides.todo.entity.Role;
import net.javaguides.todo.entity.User;
import net.javaguides.todo.exception.TodoAPIException;
import net.javaguides.todo.repository.RoleRepository;
import net.javaguides.todo.repository.UserRepository;
import net.javaguides.todo.security.JwtTokenProvider;
import net.javaguides.todo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;






@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Injection de dépendance du repository UserRepository.
    @Autowired
    private UserRepository userRepository;

    // Injection de dépendance du repository RoleRepository.
    @Autowired
    private RoleRepository roleRepository;

    // Injection de dépendance de l'encodeur de mot de passe PasswordEncoder.
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Injection de dépendance du gestionnaire d'authentification AuthenticationManager.
    @Autowired
    private AuthenticationManager authenticationManager;

    // Injection de dépendance du fournisseur de jeton JWT JwtTokenProvider.
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Méthode pour l'inscription d'un nouvel utilisateur à partir des informations de RegisterDto.
    @Override
    public String register(RegisterDto registerDto) {

        // Vérifie si le nom d'utilisateur existe déjà dans la base de données.
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        // Vérifie si l'adresse e-mail existe déjà dans la base de données.
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new TodoAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        // Crée une nouvelle instance de l'entité User à partir des informations du RegisterDto.
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Crée un ensemble de rôles pour l'utilisateur, dans ce cas, seul le rôle "ROLE_USER" est attribué.
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);

        // Affecte les rôles à l'utilisateur.
        user.setRoles(roles);

        // Enregistre l'utilisateur dans la base de données en utilisant le UserRepository.
        userRepository.save(user);

        return "User Registered Successfully!.";
    }

    // Méthode pour la connexion d'un utilisateur à partir des informations de LoginDto.
    @Override
    public String login(LoginDto loginDto) {

        // Authentifie l'utilisateur en utilisant l'objet AuthenticationManager.
        // Il utilise les informations de connexion fournies dans le LoginDto pour effectuer l'authentification.
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));

        // Définit l'objet Authentication comme l'authentification actuelle dans le contexte de sécurité.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Génère le jeton JWT à partir de l'authentification réussie et le renvoie.
        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }
}

