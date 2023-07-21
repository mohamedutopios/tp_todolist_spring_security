package net.javaguides.todo.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.todo.dto.TodoDto;
import net.javaguides.todo.entity.Todo;
import net.javaguides.todo.exception.ResourceNotFoundException;
import net.javaguides.todo.repository.TodoRepository;
import net.javaguides.todo.service.TodoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    // Injection de dépendance du repository TodoRepository.
    private TodoRepository todoRepository;

    // Injection de dépendance du mapper ModelMapper.
    private ModelMapper modelMapper;

    // Méthode pour ajouter un nouveau Todo en utilisant les informations fournies dans TodoDto.
    @Override
    public TodoDto addTodo(TodoDto todoDto) {

        // Convertit l'objet TodoDto en entité JPA Todo à l'aide de ModelMapper.
        Todo todo = modelMapper.map(todoDto, Todo.class);

        // Enregistre l'entité Todo dans la base de données en utilisant le TodoRepository.
        Todo savedTodo = todoRepository.save(todo);

        // Convertit l'entité Todo en objet TodoDto à l'aide de ModelMapper.
        TodoDto savedTodoDto = modelMapper.map(savedTodo, TodoDto.class);

        // Renvoie l'objet TodoDto sauvegardé.
        return savedTodoDto;
    }

    // Méthode pour obtenir un Todo à partir de son identifiant (id).
    @Override
    public TodoDto getTodo(Long id) {

        // Recherche le Todo dans la base de données à partir de son identifiant.
        // S'il n'existe pas, lève une exception ResourceNotFoundException avec un message spécifique.
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));

        // Convertit l'entité Todo en objet TodoDto à l'aide de ModelMapper.
        return modelMapper.map(todo, TodoDto.class);
    }

    // Méthode pour obtenir tous les Todos disponibles dans la base de données.
    @Override
    public List<TodoDto> getAllTodos() {

        // Récupère tous les Todos à partir de la base de données en utilisant le TodoRepository.
        List<Todo> todos = todoRepository.findAll();

        // Convertit chaque entité Todo en objet TodoDto à l'aide de ModelMapper,
        // puis collecte les objets TodoDto dans une liste et la renvoie.
        return todos.stream().map((todo) -> modelMapper.map(todo, TodoDto.class))
                .collect(Collectors.toList());
    }

    // Méthode pour mettre à jour un Todo à partir des informations fournies dans TodoDto et de son identifiant (id).
    @Override
    public TodoDto updateTodo(TodoDto todoDto, Long id) {

        // Recherche le Todo dans la base de données à partir de son identifiant.
        // S'il n'existe pas, lève une exception ResourceNotFoundException avec un message spécifique.
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id : " + id));

        // Met à jour les propriétés du Todo avec les informations fournies dans TodoDto.
        todo.setTitle(todoDto.getTitle());
        todo.setDescription(todoDto.getDescription());
        todo.setCompleted(todoDto.isCompleted());

        // Enregistre les modifications dans la base de données en utilisant le TodoRepository.
        Todo updatedTodo = todoRepository.save(todo);

        // Convertit l'entité Todo mise à jour en objet TodoDto à l'aide de ModelMapper et la renvoie.
        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    // Méthode pour supprimer un Todo à partir de son identifiant (id).
    @Override
    public void deleteTodo(Long id) {

        // Recherche le Todo dans la base de données à partir de son identifiant.
        // S'il n'existe pas, lève une exception ResourceNotFoundException avec un message spécifique.
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id : " + id));

        // Supprime le Todo de la base de données en utilisant le TodoRepository.
        todoRepository.deleteById(id);
    }

    // Méthode pour marquer un Todo comme complété à partir de son identifiant (id).
    @Override
    public TodoDto completeTodo(Long id) {

        // Recherche le Todo dans la base de données à partir de son identifiant.
        // S'il n'existe pas, lève une exception ResourceNotFoundException avec un message spécifique.
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id : " + id));

        // Marque le Todo comme complété en mettant à jour la propriété "completed" à TRUE.
        todo.setCompleted(Boolean.TRUE);

        // Enregistre les modifications dans la base de données en utilisant le TodoRepository.
        Todo updatedTodo = todoRepository.save(todo);

        // Convertit l'entité Todo mise à jour en objet TodoDto à l'aide de ModelMapper et la renvoie.
        return modelMapper.map(updatedTodo, TodoDto.class);
    }

    // Méthode pour marquer un Todo comme non complété à partir de son identifiant (id).
    @Override
    public TodoDto inCompleteTodo(Long id) {

        // Recherche le Todo dans la base de données à partir de son identifiant.
        // S'il n'existe pas, lève une exception ResourceNotFoundException avec un message spécifique.
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id : " + id));

        // Marque le Todo comme non complété en mettant à jour la propriété "completed" à FALSE.
        todo.setCompleted(Boolean.FALSE);

        // Enregistre les modifications dans la base de données en utilisant le TodoRepository.
        Todo updatedTodo = todoRepository.save(todo);

        // Convertit l'entité Todo mise à jour en objet TodoDto à l'aide de ModelMapper et la renvoie.
        return modelMapper.map(updatedTodo, TodoDto.class);
    }
}

