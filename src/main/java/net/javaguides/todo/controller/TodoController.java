package net.javaguides.todo.controller;

import lombok.AllArgsConstructor;
import net.javaguides.todo.dto.TodoDto;
import net.javaguides.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*") // Permet à cette classe de gérer les requêtes cross-origin depuis n'importe quelle origine (*).
@RestController // Indique que cette classe est un contrôleur REST.
@RequestMapping("api/todos") // Toutes les requêtes commençant par "/api/todos" seront gérées par ce contrôleur.
public class TodoController {

    @Autowired // Injecte automatiquement une instance de TodoService dans cette classe.
    private TodoService todoService;


    // Endpoint pour ajouter un todo.
    @PreAuthorize("hasRole('ADMIN')") // Permet l'accès à cette méthode uniquement aux utilisateurs ayant le rôle "ADMIN".
    @PostMapping // Cette méthode gère les requêtes POST sur "/api/todos".
    public ResponseEntity<TodoDto> addTodo(@RequestBody TodoDto todoDto) {
        // Appelle la méthode "addTodo" de TodoService en passant l'objet TodoDto pour ajouter un todo.
        TodoDto savedTodo = todoService.addTodo(todoDto);
        // Retourne une réponse HTTP avec le todo nouvellement créé et le code de statut "201 CREATED".
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer un todo par son ID.
    @PreAuthorize("hasAnyRole('ADMIN','USER')") // Permet l'accès à cette méthode aux utilisateurs ayant les rôles "ADMIN" ou "USER".
    @GetMapping("{id}") // Cette méthode gère les requêtes GET sur "/api/todos/{id}" où {id} est l'ID du todo.
    public ResponseEntity<TodoDto> getTodo(@PathVariable("id") Long todoId) {
        // Appelle la méthode "getTodo" de TodoService en passant l'ID du todo à récupérer.
        TodoDto todoDto = todoService.getTodo(todoId);
        // Retourne une réponse HTTP avec le todo récupéré et le code de statut "200 OK".
        return new ResponseEntity<>(todoDto, HttpStatus.OK);
    }

    // Endpoint pour récupérer tous les todos.
    @PreAuthorize("hasAnyRole('ADMIN','USER')") // Permet l'accès à cette méthode aux utilisateurs ayant les rôles "ADMIN" ou "USER".
    @GetMapping // Cette méthode gère les requêtes GET sur "/api/todos".
    public ResponseEntity<List<TodoDto>> getAllTodos() {
        // Appelle la méthode "getAllTodos" de TodoService pour récupérer tous les todos.
        List<TodoDto> todos = todoService.getAllTodos();
        // Retourne une réponse HTTP avec la liste de tous les todos et le code de statut "200 OK".
        return ResponseEntity.ok(todos);
    }

    // Endpoint pour mettre à jour un todo.
    @PreAuthorize("hasRole('ADMIN')") // Permet l'accès à cette méthode uniquement aux utilisateurs ayant le rôle "ADMIN".
    @PutMapping("{id}") // Cette méthode gère les requêtes PUT sur "/api/todos/{id}" où {id} est l'ID du todo à mettre à jour.
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoDto todoDto, @PathVariable("id") Long todoId) {
        // Appelle la méthode "updateTodo" de TodoService en passant l'objet TodoDto à mettre à jour et l'ID du todo.
        TodoDto updatedTodo = todoService.updateTodo(todoDto, todoId);
        // Retourne une réponse HTTP avec le todo mis à jour et le code de statut "200 OK".
        return ResponseEntity.ok(updatedTodo);
    }

    // Endpoint pour supprimer un todo.
    @PreAuthorize("hasRole('ADMIN')") // Permet l'accès à cette méthode uniquement aux utilisateurs ayant le rôle "ADMIN".
    @DeleteMapping("{id}") // Cette méthode gère les requêtes DELETE sur "/api/todos/{id}" où {id} est l'ID du todo à supprimer.
    public ResponseEntity<String> deleteTodo(@PathVariable("id") Long todoId) {
        // Appelle la méthode "deleteTodo" de TodoService en passant l'ID du todo à supprimer.
        todoService.deleteTodo(todoId);
        // Retourne une réponse HTTP avec un message de succès et le code de statut "200 OK".
        return ResponseEntity.ok("Todo deleted successfully!.");
    }

    // Endpoint pour marquer un todo comme terminé.
    @PreAuthorize("hasAnyRole('ADMIN','USER')") // Permet l'accès à cette méthode aux utilisateurs ayant les rôles "ADMIN" ou "USER".
    @PatchMapping("{id}/complete") // Cette méthode gère les requêtes PATCH sur "/api/todos/{id}/complete" où {id} est l'ID du todo.
    public ResponseEntity<TodoDto> completeTodo(@PathVariable("id") Long todoId) {
        // Appelle la méthode "completeTodo" de TodoService en passant l'ID du todo à marquer comme terminé.
        TodoDto updatedTodo = todoService.completeTodo(todoId);
        // Retourne une réponse HTTP avec le todo mis à jour (marqué comme terminé) et le code de statut "200 OK".
        return ResponseEntity.ok(updatedTodo);
    }

    // Endpoint pour marquer un todo comme non terminé (incomplet).
    @PreAuthorize("hasAnyRole('ADMIN','USER')") // Permet l'accès à cette méthode aux utilisateurs ayant les rôles "ADMIN" ou "USER".
    @PatchMapping("{id}/in-complete") // Cette méthode gère les requêtes PATCH sur "/api/todos/{id}/in-complete" où {id} est l'ID du todo.
    public ResponseEntity<TodoDto> inCompleteTodo(@PathVariable("id") Long todoId) {
        // Appelle la méthode "inCompleteTodo" de TodoService en passant l'ID du todo à marquer comme non terminé.
        TodoDto updatedTodo = todoService.inCompleteTodo(todoId);
        // Retourne une réponse HTTP avec le todo mis à jour (marqué comme non terminé) et le code de statut "200 OK".
        return ResponseEntity.ok(updatedTodo);
    }

}
