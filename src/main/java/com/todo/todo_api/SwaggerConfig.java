package com.todo.todo_api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo API")
                        .version("1.0.0")
                        .description("""
                                API REST de gestion de tâches.
                                
                                ## Fonctionnalités
                                - Créer, lire, mettre à jour et supprimer des tâches
                                - Filtrer les tâches par statut
                                
                                ## Statuts disponibles
                                | Statut | Description |
                                |--------|-------------|
                                | `TODO` | Tâche à faire |
                                | `IN_PROGRESS` | Tâche en cours |
                                | `DONE` | Tâche terminée |
                                """)
                        .contact(new Contact()
                                .name("Diallo")
                                .email("diallo@todo.com")));
    }
}