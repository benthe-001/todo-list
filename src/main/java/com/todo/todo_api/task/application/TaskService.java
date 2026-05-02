package com.todo.todo_api.task.application;

import com.todo.todo_api.task.application.dto.TaskRequest;
import com.todo.todo_api.task.application.dto.TaskResponse;
import com.todo.todo_api.task.domain.Task;
import com.todo.todo_api.task.domain.TaskStatus;
import com.todo.todo_api.task.infrastructure.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse create(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .build();
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> findAll(TaskStatus status) {
        List<Task> tasks = (status != null)
                ? taskRepository.findByStatus(status)
                : taskRepository.findAll();
        return tasks.stream().map(this::toResponse).toList();
    }

    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + id));
        return toResponse(task);
    }

    public TaskResponse update(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable : " + id));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        return toResponse(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }
}