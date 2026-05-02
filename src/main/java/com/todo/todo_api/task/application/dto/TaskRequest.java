package com.todo.todo_api.task.application.dto;

import com.todo.todo_api.task.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String description;

    @NotNull(message = "Le statut est obligatoire")
    private TaskStatus status;
}