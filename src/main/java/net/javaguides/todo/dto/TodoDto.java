package net.javaguides.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
// Ce DTO va servir à stocker lors de la requête et des responses concernant les todo.
public class TodoDto {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
}
