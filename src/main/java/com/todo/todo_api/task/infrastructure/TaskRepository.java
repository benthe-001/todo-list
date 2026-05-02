package com.todo.todo_api.task.infrastructure;

import com.todo.todo_api.task.domain.Task;
import com.todo.todo_api.task.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
}