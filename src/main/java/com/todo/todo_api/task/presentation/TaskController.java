package com.todo.todo_api.task.presentation;

import com.todo.todo_api.task.application.TaskService;
import com.todo.todo_api.task.application.dto.TaskRequest;
import com.todo.todo_api.task.application.dto.TaskResponse;
import com.todo.todo_api.task.domain.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(
        name = "Gestion des tâches",
        description = "Endpoints pour créer, lire, mettre à jour et supprimer des tâches"
)
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Créer une nouvelle tâche",
            description = "Crée une tâche avec un titre, une description et un statut. Le statut doit être TODO, IN_PROGRESS ou DONE."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tâche créée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponse.class),
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "title": "Apprendre Spring Boot",
                      "description": "Suivre le tutoriel complet",
                      "status": "TODO"
                    }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides — titre ou statut manquant",
                    content = @Content()
            )
    })
    public TaskResponse create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Les informations de la tâche à créer",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                    {
                      "title": "Apprendre Spring Boot",
                      "description": "Suivre le tutoriel complet",
                      "status": "TODO"
                    }
                    """)
                    )
            )
            @Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @GetMapping
    @Operation(
            summary = "Lister toutes les tâches",
            description = "Retourne la liste de toutes les tâches. Vous pouvez filtrer par statut en passant le paramètre `status`."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste des tâches retournée avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    [
                      {
                        "id": 1,
                        "title": "Apprendre Spring Boot",
                        "description": "Suivre le tutoriel complet",
                        "status": "TODO"
                      },
                      {
                        "id": 2,
                        "title": "Faire les tests",
                        "description": "Écrire les tests unitaires",
                        "status": "IN_PROGRESS"
                      }
                    ]
                    """)
                    )
            )
    })
    public List<TaskResponse> findAll(
            @Parameter(
                    description = "Filtrer les tâches par statut",
                    example = "TODO",
                    schema = @Schema(allowableValues = {"TODO", "IN_PROGRESS", "DONE"})
            )
            @RequestParam(required = false) TaskStatus status) {
        return taskService.findAll(status);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer une tâche par son ID",
            description = "Retourne les détails d'une tâche spécifique à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tâche trouvée",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "title": "Apprendre Spring Boot",
                      "description": "Suivre le tutoriel complet",
                      "status": "TODO"
                    }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Tâche introuvable pour cet ID",
                    content = @Content()
            )
    })
    public TaskResponse findById(
            @Parameter(description = "L'identifiant de la tâche", example = "1", required = true)
            @PathVariable Long id) {
        return taskService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour une tâche",
            description = "Modifie le titre, la description ou le statut d'une tâche existante."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tâche mise à jour avec succès",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "title": "Apprendre Spring Boot",
                      "description": "Tutoriel terminé !",
                      "status": "DONE"
                    }
                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Tâche introuvable pour cet ID",
                    content = @Content()
            )
    })
    public TaskResponse update(
            @Parameter(description = "L'identifiant de la tâche à modifier", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Les nouvelles informations de la tâche",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(value = """
                    {
                      "title": "Apprendre Spring Boot",
                      "description": "Tutoriel terminé !",
                      "status": "DONE"
                    }
                    """)
                    )
            )
            @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Supprimer une tâche",
            description = "Supprime définitivement une tâche à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Tâche supprimée avec succès"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Tâche introuvable pour cet ID",
                    content = @Content()
            )
    })
    public void delete(
            @Parameter(description = "L'identifiant de la tâche à supprimer", example = "1", required = true)
            @PathVariable Long id) {
        taskService.delete(id);
    }
}