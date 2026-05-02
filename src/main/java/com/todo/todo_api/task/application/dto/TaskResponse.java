package com.todo.todo_api.task.application.dto;

import com.todo.todo_api.task.domain.TaskStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
}