package com.todo.todo_api.task.application;

import com.todo.todo_api.task.application.dto.TaskRequest;
import com.todo.todo_api.task.application.dto.TaskResponse;
import com.todo.todo_api.task.domain.Task;
import com.todo.todo_api.task.domain.TaskStatus;
import com.todo.todo_api.task.infrastructure.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequest request;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test tâche")
                .description("Description test")
                .status(TaskStatus.TODO)
                .build();

        request = new TaskRequest();
        request.setTitle("Test tâche");
        request.setDescription("Description test");
        request.setStatus(TaskStatus.TODO);
    }

    @Test
    void create_devraitRetournerTaskResponse() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test tâche");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void findAll_sansFiltre_devraitRetournerToutesLesTaches() {
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<TaskResponse> responses = taskService.findAll(null);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("Test tâche");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void findAll_avecFiltre_devraitRetournerTachesParStatut() {
        when(taskRepository.findByStatus(TaskStatus.TODO)).thenReturn(List.of(task));

        List<TaskResponse> responses = taskService.findAll(TaskStatus.TODO);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getStatus()).isEqualTo(TaskStatus.TODO);
        verify(taskRepository, times(1)).findByStatus(TaskStatus.TODO);
    }

    @Test
    void findById_devraitRetournerTaskResponse() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void findById_tacheInexistante_devraitLeverException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tâche introuvable");
    }

    @Test
    void update_devraitModifierEtRetournerTaskResponse() {
        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Titre modifié");
        updateRequest.setDescription("Description modifiée");
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        Task taskMaj = Task.builder()
                .id(1L)
                .title("Titre modifié")
                .description("Description modifiée")
                .status(TaskStatus.IN_PROGRESS)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(taskMaj);

        TaskResponse response = taskService.update(1L, updateRequest);

        assertThat(response.getTitle()).isEqualTo("Titre modifié");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void delete_devraitAppelerDeleteById() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.delete(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}